package xyz.parkh.challenge.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.model.UpdateUserDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public User getUser(String authId) {
        return authService.findByAuthId(authId);
    }

    public User getUser(long userId) {
        return authService.findById(userId);
    }


    @Transactional
    public void update(UpdateUserDto updateUserDto) {
        String authId = updateUserDto.getTargetAuthId();
        User findUser = authService.findByAuthId(authId);
        updateUserDto.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));

        findUser.update(updateUserDto);
    }
}
