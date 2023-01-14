package xyz.parkh.challenge.api.controller.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.api.controller.challenge.model.ChallengeShortInfoForCommon;
import xyz.parkh.challenge.api.controller.challenge.model.request.GetChallengesByCommonRequest;
import xyz.parkh.challenge.api.controller.challenge.model.response.GetChallengeDetailResponse;
import xyz.parkh.challenge.api.controller.challenge.model.response.GetChallengesByCommonResponse;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenge")
public class CommonChallengeController {
    private final ChallengeService challengeService;

    /**
     * 챌린지 목록 조회
     *
     * @param request
     * @return
     */
    @GetMapping
    public Result<GetChallengesByCommonResponse> getChallenges(@ModelAttribute @Validated GetChallengesByCommonRequest request) {
        List<Challenge> challenges = challengeService.getChallengeList(request.getCategory(), request.getStatus());
        List<ChallengeShortInfoForCommon> challengeShortInfosForCommon = challenges.stream()
                .map(challenge -> new ChallengeShortInfoForCommon(challenge)).collect(Collectors.toList());

        return Result.<GetChallengesByCommonResponse>builder().data(new GetChallengesByCommonResponse(challengeShortInfosForCommon)).build();
    }

    /**
     * 챌린지 상세 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<GetChallengeDetailResponse> getChallengeDetail(@PathVariable("id") long id) {
        Challenge challenge = challengeService.getChallenge(id);

        return Result.<GetChallengeDetailResponse>builder().data(new GetChallengeDetailResponse(challenge)).build();
    }
}
