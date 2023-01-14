package xyz.parkh.challenge.domain.user.service;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

@Getter
public class CustomUserDetails extends User {
    xyz.parkh.challenge.domain.user.entity.User user;

    public CustomUserDetails(xyz.parkh.challenge.domain.user.entity.User user) {
        super(user.getAuthId(), user.getPassword(), user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getUserType().getName())).collect(Collectors.toList()));
        this.user = user;
    }

    public CustomUserDetails(User user) {
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
