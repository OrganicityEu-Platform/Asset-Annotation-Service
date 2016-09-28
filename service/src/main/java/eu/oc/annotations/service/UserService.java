package eu.oc.annotations.service;

import eu.oc.annotations.config.SpringUserDetails;
import eu.oc.annotations.domain.User;
import eu.oc.annotations.dto.UserDTO;
import eu.oc.annotations.dto.converter.UserToUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public final class UserService {

    private final UserRepository userRepository;

    private final UserToUserDTO userDTOConverter;


    @Autowired
    public UserService(
              final UserRepository userRepository,

             UserToUserDTO userDTOConverter) {

        this.userRepository = userRepository;

        this.userDTOConverter = userDTOConverter;
    }


    public UserDTO findUser(  final Long userId) {
        final User user = userRepository.findOne(userId);
        return userDTOConverter.convert(user);
    }

    public UserDTO findUserByEmail( final String email) {
        final User user = userRepository.findByEmailEquals(email);
        return userDTOConverter.convert(user);
    }





    public void refreshAuthenticatedUser() {
        final Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication.getPrincipal() instanceof SpringUserDetails) {
            final SpringUserDetails currentPrincipal = (SpringUserDetails) currentAuthentication.getPrincipal();
            final User refreshedUser = userRepository.findOne(currentPrincipal.getUserDTO().getId());
            final UserDTO userDTO = userDTOConverter.convert(refreshedUser);
            final SpringUserDetails refreshedPrincipal = new SpringUserDetails(userDTO, userDTO.getId(), refreshedUser.getPassword());
            final Authentication newAuthentication = new UsernamePasswordAuthenticationToken(refreshedPrincipal, refreshedUser.getPassword(), currentPrincipal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        } else {
            System.out.println("The currently-authenticated principal is not an instance of type SecurityConfig.SpringUserDetails");
        }
    }

    public boolean verifyPassword(
            final UserDTO userDTO,
              final String password
    ) {
        final User user = userRepository.findOne(userDTO.getId());
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, user.getPassword());
    }


    public String encryptPassword(  final String rawPassword) {
        final String salt = BCrypt.gensalt(10, new SecureRandom());
      //  return BCrypt.hashpw(rawPassword, salt);
        return rawPassword;
    }


    public String getPasswordHash(  final UserDTO userDTO) {
        final User user = userRepository.findOne(userDTO.getId());
        return user.getPassword();
    }


    public String getPasswordHashForNewUser(  final UserDTO userDTO) {
        final User user = userRepository.findByEmailEquals(userDTO.getEmail());
        return user.getPassword();
    }

}
