package xyz.parkh.challenge.api.controller.point;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.api.controller.point.model.response.ChargePointResponse;
import xyz.parkh.challenge.api.controller.point.model.response.GetAmountResponse;
import xyz.parkh.challenge.domain.point.entity.PointHistory;
import xyz.parkh.challenge.domain.point.service.PointService;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
@PreAuthorize("hasAnyRole('CHALLENGER', 'ADMIN')")
public class PointController {
    private final PointService pointService;
    private final UserService userService;

    @GetMapping("/charge/{pointAmount}")
    public Result<ChargePointResponse> chargePoint(@PathVariable("pointAmount") long amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authId = authentication.getName();
        User user = userService.getUser(authId);

        PointHistory pointHistory = pointService.chargePoint(user, amount);

        return Result.<ChargePointResponse>builder().data(new ChargePointResponse(pointHistory.getId())).build();
    }

    @GetMapping("/user")
    public Result<GetAmountResponse> getAmount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authId = authentication.getName();
        User user = userService.getUser(authId);

        Long amount = pointService.getPointAmount(user);

        return Result.<GetAmountResponse>builder().data(new GetAmountResponse(amount)).build();
    }
}
