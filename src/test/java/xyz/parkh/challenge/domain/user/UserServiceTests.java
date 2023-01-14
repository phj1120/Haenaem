package xyz.parkh.challenge.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.domain.user.service.AuthService;
import xyz.parkh.challenge.domain.user.service.UserService;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class UserServiceTests {
    private final UserService userService;
    private final AuthService authService;

//    @Test
//    public void 유저_정보_조회() {
//        User user = new User("authId", "password", "name", "AAA@AAA.com", UserType.ROLE_CHALLENGER);
//        User signUpUser = authService.signUp(user);
//
//        User getUser = userService.getUser("authId");
//        assertEquals(signUpUser, getUser);
//    }
//
//    @Test
//    public void 유저_정보_수정() {
//        String authId = "authId";
//        String password = "password";
//        String name = "name";
//        String email = "AAA@AAA.com";
//        User user = new User(authId, password, name, email, UserType.ROLE_CHALLENGER);
//        authService.signUp(user);
//
//        String newPassword = "newPassword";
//        String newName = "newName";
//        UpdateUserDto updateUserDto = UpdateUserDto.builder()
//                .targetAuthId(user.getAuthId())
//                .name(newName)
//                .password(newPassword)
//                .build();
//        userService.update(updateUserDto);
//
//        User updateUser = userService.getUser(authId);
//        assertEquals(updateUser.getName(), updateUserDto.getName());
//
//        SignInDto signInDto = new SignInDto(authId, password);
//        assertThrows(IllegalArgumentException.class, () -> authService.signIn(signInDto));
//
//        SignInResultDto signInUserByNewPassword = authService.signIn(new SignInDto(authId, newPassword));
//        assertNotNull(signInUserByNewPassword);
//    }
}
