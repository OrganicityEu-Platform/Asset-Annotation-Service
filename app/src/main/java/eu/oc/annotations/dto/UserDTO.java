package eu.oc.annotations.dto;

import eu.oc.annotations.handlers.PasswordMatches;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@PasswordMatches
public final class UserDTO implements Serializable {

    private long id;


    private Integer role;


    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 6)
    private String password;

    //@NotNull
    //@NotEmpty
    private String matchingPassword;


    @NotNull
    @NotEmpty
    private String email;


    @NotNull
    @NotEmpty
    private String firstname;

    @NotNull
    @NotEmpty
    private String lastname;


    public UserDTO(
             final Long id,

            final String email,
            final Integer role,

            final String username,
            final String password

    ) {
        this.id = id;


        this.email = email;
        this.role = role;

        this.username = username;
        this.password = password;

    }

    public UserDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    @Override
    public boolean equals(final Object other) {
        boolean equals = false;
        if (other instanceof UserDTO) {
            final UserDTO that = (UserDTO) other;
            equals = this.getId() == that.getId()
                    && this.getEmail().equals(that.getEmail())
            ;
        }
        return equals;
    }

    /*
    @Override
    public int hashCode() {
        final int idHash = (id == null) ? 0 : id.hashCode();

        final int emailHash = (email == null) ? 0 : email.hashCode();
        final int roleHash = (role == null) ? 0 : role.hashCode();

        return idHash + emailHash + roleHash;
    }
*/
}
