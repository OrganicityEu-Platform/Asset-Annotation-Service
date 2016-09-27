package eu.oc.annotations.service;


import eu.oc.annotations.config.SpringUserDetails;
import eu.oc.annotations.domain.User;
import eu.oc.annotations.dto.UserDTO;
import eu.oc.annotations.dto.converter.UserToUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




@Service
public class CurrentUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);
    private final UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserToUserDTO userDTOConverter;




    @Autowired
    public CurrentUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public SpringUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.debug("Authenticating user with username={}", username.replaceFirst("@.*", "@***"));
        if (userRepository == null) {
            System.out.println("NULL USER REPOSITORY");
        }
        final User user = userRepository.findByUsernameEquals(username);
        if (user == null) return null;
        final UserDTO userDTO = userDTOConverter.convert(user);



        return new SpringUserDetails(userDTO, userDTO.getId(), user.getPassword() );
    }

    public static SpringUserDetails getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SpringUserDetails) {
            return (SpringUserDetails) authentication.getPrincipal();
        } else {
            return null;
        }
    }

    public static UserDTO getCurrentUserByPrincipal() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SpringUserDetails) {
            return ((SpringUserDetails) authentication.getPrincipal()).getUserDTO();
        } else {
            return null;
        }
    }


}