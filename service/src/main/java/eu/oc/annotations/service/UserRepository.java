package eu.oc.annotations.service;

import eu.oc.annotations.domain.User;
import org.springframework.stereotype.Service;



@Service
public class UserRepository {


    public static User findByEmailEquals( String email) {
        return dummyUser();
    }


    public static User findByUsernameEquals( String username) {
        return dummyUser();
    }


    public static User findOne(Long id) {
        return dummyUser();
    }


    public static User dummyUser() {
        User u = new User();
        u.setEmail("test");
        u.setId(1L);
        u.setUsername("test");
        u.setPassword("$2a$08$G/hS4Gjg7GfHyEyNbF53qulorqJm.iJwnqaP/7aSWTv1/6df1GUty");
        u.setRole(1);
        return u;
    }

}
