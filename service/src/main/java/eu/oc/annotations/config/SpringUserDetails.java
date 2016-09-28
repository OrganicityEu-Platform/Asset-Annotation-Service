package eu.oc.annotations.config;

import eu.oc.annotations.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by etheodor on 29/1/2015.
 */
public final class SpringUserDetails implements UserDetails {

    private final UserDTO userDTO;
    private final long userId;
    private final String password;

    public SpringUserDetails(
             final UserDTO userDTO,
            long userId,
              final String password
    ) {
        this.userDTO = userDTO;
        this.userId = userId;
        this.password = password;
    }

    @Override

    public String getUsername() {
        return this.userDTO.getEmail();
    }

    @Override

    public String getPassword() {
        return this.password;
    }


    public UserDTO getUserDTO() {
        return userDTO;
    }


    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        Integer userRoleId;
        String userRoleStr;

        userRoleId = this.getUserDTO().getRole();
        userRoleStr = "Organicity";

        /* Roles should start with "ROLE_" prefix */
        authorities.add(new SimpleGrantedAuthority(userRoleStr));

        return authorities;
    }


    public long getUserId() {
        return userId;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
