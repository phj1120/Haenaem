package xyz.parkh.challenge.api.controller.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.api.controller.challenge.model.ChallengeHistoryShortInfoForChallenger;
import xyz.parkh.challenge.api.controller.challenge.model.request.GetChallengesForChallengerRequest;
import xyz.parkh.challenge.api.controller.challenge.model.response.CancelChallengeResponse;
import xyz.parkh.challenge.api.controller.challenge.model.response.GetChallengesForChallengerResponse;
import xyz.parkh.challenge.api.controller.challenge.model.response.RequestChallengeResponse;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.user.entity.User;
import xyz.parkh.challenge.domain.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenger")
@PreAuthorize("hasAnyRole('CHALLENGER')")
public class ChallengerChallengeController {
    private final ChallengeService challengeService;
    private final UserService userService;

    /**
     * 챌린지 신청
     *
     * @param
     * @return
     */
    @GetMapping("/challenge/{challengeId}/request")
    public Result<RequestChallengeResponse> requestChallenge(@PathVariable("challengeId") Long challengeId) {
        // TODO 반복 되는 기능으로 Component 에 등록해서 현재 사용자 가져오는 기능 추가하면 좋을 듯
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authId = authentication.getName();
        User user = userService.getUser(authId);

        Challenge challenge = challengeService.getChallenge(challengeId);

        ChallengeHistory challengeHistory = challengeService.requestChallenge(challenge, user);
        return Result.<RequestChallengeResponse>builder().data(new RequestChallengeResponse(challengeHistory)).build();
    }

    /**
     * 챌린지 신청 취소
     *
     * @param
     * @return
     */
    @GetMapping("/challenge/{challengeId}/cancel")
    public Result<CancelChallengeResponse> cancelChallenge(@PathVariable("challengeId") Long challengeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authId = authentication.getName();
        User user = userService.getUser(authId);

        Challenge challenge = challengeService.getChallenge(challengeId);

        challengeService.cancelChallenge(challenge, user);
        return Result.<CancelChallengeResponse>builder().data(new CancelChallengeResponse("ok")).build();
    }

    /**
     * 챌린지 조회
     *
     * @param
     * @return
     */
    @GetMapping("/challenge")
    public Result<GetChallengesForChallengerResponse> getChallenges(@ModelAttribute @Validated GetChallengesForChallengerRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authId = authentication.getName();
        User user = userService.getUser(authId);

        List<ChallengeHistoryShortInfoForChallenger> challengeShortInfoForManagerList = challengeService.getFinallyChallengeHistories(user, request.getStatus()).stream()
                .map(challenge -> new ChallengeHistoryShortInfoForChallenger(challenge))
                .collect(Collectors.toList());

        return Result.<GetChallengesForChallengerResponse>builder().data(new GetChallengesForChallengerResponse(challengeShortInfoForManagerList)).build();
    }
}
