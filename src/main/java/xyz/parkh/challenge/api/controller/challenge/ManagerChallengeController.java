package xyz.parkh.challenge.api.controller.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.api.controller.challenge.model.ChallengeShortInfoForManager;
import xyz.parkh.challenge.api.controller.challenge.model.UserInfo;
import xyz.parkh.challenge.api.controller.challenge.model.request.*;
import xyz.parkh.challenge.api.controller.challenge.model.response.*;
import xyz.parkh.challenge.domain.challenge.ChallengeService;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;
import xyz.parkh.challenge.domain.challenge.model.HistoryType;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
@PreAuthorize("hasAnyRole('ADMIN')")
public class ManagerChallengeController {
    private final ChallengeService challengeService;


    /**
     * 챌린지 목록 조회
     *
     * @param request
     * @return
     */
    @GetMapping("/challenge")
    public Result<GetChallengesForManagerResponse> getChallenges(@ModelAttribute @Validated GetChallengesForMangerRequest request) {
        List<Challenge> challenges = challengeService.getChallengeList(request.getStatus());
        List<ChallengeShortInfoForManager> challengeShortInfos = challenges.stream()
                .map(challenge -> new ChallengeShortInfoForManager(challenge))
                .collect(Collectors.toList());

        return Result.<GetChallengesForManagerResponse>builder().data(new GetChallengesForManagerResponse(challengeShortInfos)).build();
    }

    /**
     * 챌린지 생성
     *
     * @param request 챌린지 셋 아이디, 챌린지 시작일, 기수 이름
     * @return
     */
    @PostMapping("/challenge")
    public Result<CreateChallengeResponse> createChallenge(@RequestBody @Validated CreateChallengeRequest request) {
        Challenge challenge = challengeService.addChallenge(request.convert());

        return Result.<CreateChallengeResponse>builder().data(new CreateChallengeResponse(challenge.getId())).build();
    }

    /**
     * 카테고리 조회
     *
     * @return
     */
    @GetMapping("/category")
    public Result<GetCategoryResponse> getCategory() {
        List<String> categories = challengeService.getChallengeSetCategory().stream()
                .map(category -> category.getName())
                .collect(Collectors.toList());

        return Result.<GetCategoryResponse>builder().data(new GetCategoryResponse(categories)).build();
    }


    /**
     * 카테고리별 챌린지 셋 조회
     *
     * @param request
     * @return
     */
    @GetMapping("/category/challengeSet")
    public Result<GetChallengeSetResponse> getChallengeSet(@ModelAttribute @Validated GetChallengeSetRequest request) {
        ChallengeCategory category = request.getCategory();

        List<ChallengeSet> challengeSets = challengeService.getChallengeSetByCategory(category);

        return Result.<GetChallengeSetResponse>builder().data(new GetChallengeSetResponse(challengeSets)).build();
    }

    /**
     * 해당 챌린지에 참여중인 사용자 조회
     *
     * @param request
     * @return
     */
    @GetMapping("/challenge/users")
    public Result<GetUsersResponse> getUsers(@ModelAttribute @Validated GetUsersRequest request) {
        long challengeId = request.getId();
        HistoryType historyType = request.getType();

        Challenge challenge = challengeService.getChallenge(challengeId);
        List<UserInfo> userInfos = challengeService.findChallengers(challenge, historyType).stream()
                .map(user -> new UserInfo(user))
                .collect(Collectors.toList());

        return Result.<GetUsersResponse>builder().data(new GetUsersResponse(userInfos)).build();
    }

    /**
     * 챌린지 셋 생성
     *
     * @param request
     * @return
     */
    @PostMapping("/challenge-set")
    public Result<CreateChallengeSetResponse> CreateChallengeSet(@ModelAttribute @Validated CreateChallengeSetRequest request) {
        ChallengeSet challengeSet = challengeService.addChallengeSet(request.convertDto());

        return Result.<CreateChallengeSetResponse>builder().data(new CreateChallengeSetResponse(challengeSet)).build();
    }
}
