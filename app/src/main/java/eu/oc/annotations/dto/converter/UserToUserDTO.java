package eu.oc.annotations.dto.converter;


import eu.oc.annotations.domain.User;
import eu.oc.annotations.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public final class UserToUserDTO implements Converter<User, UserDTO> {


    @Override

    public UserDTO convert(final User user) {
        UserDTO dto = null;
        if (user != null) {
            dto = new UserDTO(
                    user.getId(),

                    user.getEmail(),
                    user.getRole(),

                    user.getUsername(),
                    user.getPassword()
            );
        }
        return dto;
    }


}
