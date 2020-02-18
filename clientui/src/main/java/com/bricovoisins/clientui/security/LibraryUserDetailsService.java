package com.bricovoisins.clientui.security;

import com.bricovoisins.clientui.beans.UserBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LibraryUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<UserBean> users = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9001/users", UserBean[].class).getBody());
        Optional<UserBean> user = users.stream().filter(u -> u.getEmail().equals(email)).findAny();
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found by email: " + email);
        }

        return buildUserDetails(user.get());
    }

    public UserDetails buildUserDetails(UserBean user) {
        return User.withUsername(user.getEmail()).password("{bcrypt}" + user.getPassword()).roles("USER").build();
    }
}
