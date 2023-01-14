package xyz.parkh.challenge.domain.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.*;
import xyz.parkh.challenge.domain.challenge.repository.ChallengeHistoryRepository;
import xyz.parkh.challenge.domain.challenge.repository.ChallengeRepository;
import xyz.parkh.challenge.domain.challenge.repository.ChallengeSetRepository;
import xyz.parkh.challenge.exception.ErrorCode;
import xyz.parkh.challenge.domain.image.service.ImageService;
import xyz.parkh.challenge.domain.point.entity.PointHistory;
import xyz.parkh.challenge.domain.point.service.PointService;
import xyz.parkh.challenge.domain.user.entity.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/*
일단 기존 있는 것만 돌려쓰는 것으로.
챌린지 셋 생성, 수정, 삭제는 안 함.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeHistoryRepository challengeHistoryRepository;
    private final ChallengeSetRepository challengeSetRepository;
    private final PointService pointService;
    private final ImageService imageService;

    // 챌린지 셋 생성
    public ChallengeSet addChallengeSet(AddChallengeSetDto dto) {
        String name = dto.getName();
        String info = dto.getInfo();
        Long pointAmount = dto.getPointAmount();
        ChallengeCategory category = dto.getCategory();
        MultipartFile image = dto.getImage();
        String content = dto.getContent();
        String description = dto.getDescription();
        ChallengePeriodType periodType = dto.getPeriodType();
        int subjectNumberOfWeek = dto.getSubjectNumberOfWeek();
        String imageUrl;

        try {
            if (dto.getImage() == null) {
                imageUrl = "defaultImage";
            } else {
                imageUrl = imageService.saveChallengeImage(image);
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILE_ERROR.getMessage());
        }

        ChallengeSet challengeSet = new ChallengeSet(name, info, pointAmount, periodType, subjectNumberOfWeek,
                imageUrl, category, content, description);
        challengeSetRepository.save(challengeSet);

        return challengeSet;
    }

    // 챌린지셋 카테고리 조회(챌린지 기수 생성용)
    public List<ChallengeCategory> getChallengeSetCategory() {
        return Arrays.stream(ChallengeCategory.values()).collect(Collectors.toList());
    }

    // 카테고리로 챌린지 셋 조회
    public List<ChallengeSet> getChallengeSetByCategory(ChallengeCategory category) {
        return challengeSetRepository.findByCategory(category);
    }

    // 챌린지 기수 생성 : static 메서드로 챌린지 만들고 추가.
    public Challenge addChallenge(Challenge challenge) {
        challengeRepository.save(challenge);
        return challenge;
    }

    // 챌린지 목록 조회(진행 상태)
    public List<Challenge> getChallengeList(ChallengeProgressStatus status) {
        List<Challenge> challengeList = challengeRepository.findAll();
        System.out.println(challengeList);
        return challengeList.stream()
                .filter(challenge -> {
                    return challenge.getStatus() == status;
                })
                .collect(Collectors.toList());
    }

    // 챌린지 목록 조회 (카테고리, 진행 상태)
    public List<Challenge> getChallengeList(ChallengeCategory category, ChallengeProgressStatus status) {
        List<Challenge> challengeList = challengeRepository.findByCategory(category);
        return challengeList.stream()
                .filter(challenge -> {
                    return challenge.getStatus() == status;
                })
                .collect(Collectors.toList());
    }

    // 진행중인 챌린지 목록 조회 : 챌린지 목록 조회 이용해 (카테고리) 에 해당하는 진행중인 챌린지 목록 조회
    public List<Challenge> getChallengeListChallenging(ChallengeCategory category) {
        List<Challenge> challengeList = getChallengeList(category, ChallengeProgressStatus.CHALLENGING);
        return challengeList;
    }

    // 챌린저 조회
    public Set<User> findChallengers(Challenge challenge, HistoryType type) {
        List<ChallengeHistory> challengeHistories = challengeHistoryRepository.findAllByChallenge(challenge);

        if (type == HistoryType.REQUEST)
            return getFinallyRequestChallengers(challengeHistories);

        return challengeHistories.stream()
                .filter(challengeHistory -> challengeHistory.getType() == type)
                .map(challengeHistory -> challengeHistory.getUser())
                .collect(Collectors.toSet());
    }

    // 최종적으로 신청한 챌린저 조회(신청 취소한 챌린저 제외)
    private Set<User> getFinallyRequestChallengers(List<ChallengeHistory> challengeHistories) {
        Set<User> requestUsers = new HashSet<>();
        for (ChallengeHistory challengeHistory : challengeHistories) {
            User user = challengeHistory.getUser();
            if (challengeHistory.getType() == HistoryType.REQUEST)
                requestUsers.add(user);
            if (challengeHistory.getType() == HistoryType.CANCEL)
                requestUsers.remove(user);
        }
        return requestUsers;
    }


    /* 사용자 기능 */
    // 최종적으로 challengeProgressStatus 중인 챌린지 기록 조회
    public List<ChallengeHistory> getFinallyChallengeHistories(User user, ChallengeProgressStatus challengeProgressStatus) {
        List<ChallengeHistory> challengeHistories = challengeHistoryRepository.findAllByUser(user);
        List<ChallengeHistory> finallyChallenges = new ArrayList<>();

        // 모집 중인 챌린지 == 사용자가 최종적으로 신청한 챌린지
        if (challengeProgressStatus == ChallengeProgressStatus.RECRUITING) {
            for (ChallengeHistory challengeHistory : challengeHistories) {
                if (challengeHistory.getType() == HistoryType.REQUEST) {
                    finallyChallenges.add(challengeHistory);
                }
                if (challengeHistory.getType() == HistoryType.CANCEL) {
                    ChallengeHistory requestChallengeHistory = getRecentRequestChallengeHistory(finallyChallenges, challengeHistory.getChallenge());
                    finallyChallenges.remove(requestChallengeHistory);
                }
            }
            return finallyChallenges;
        }

        // 진행 중인 챌린지 == 시작한 챌린지
        if (challengeProgressStatus == ChallengeProgressStatus.CHALLENGING) {
            for (ChallengeHistory challengeHistory : challengeHistories) {
                if (challengeHistory.getType() == HistoryType.START) {
                    finallyChallenges.add(challengeHistory);
                }
            }
            return finallyChallenges;
        }

        // 완료한 챌린지 == 종료한 챌린지
        for (ChallengeHistory challengeHistory : challengeHistories) {
            if (challengeHistory.getType() == HistoryType.END) {
                finallyChallenges.add(challengeHistory);
            }
        }

        return finallyChallenges;
    }

    private ChallengeHistory getRecentRequestChallengeHistory(List<ChallengeHistory> finallyChallenges, Challenge challenge) {
        for (ChallengeHistory challengeHistory : finallyChallenges) {
            if (challenge.equals(challengeHistory.getChallenge())) {
                return challengeHistory;
            }
        }

        throw new IllegalArgumentException(ErrorCode.NO_APPLY.getMessage());
    }

    // 챌린지 신청
    public ChallengeHistory requestChallenge(Challenge challenge, User user) {
        if (isUserRequestingStateChallenge(user, challenge)) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_APPLY.getMessage());
        }

        Long point = challenge.getChallengeSet().getPointAmount();
        ChallengeHistory requestChallengeHistory = user.requestChallenge(challenge);

        PointHistory pointHistory = pointService.usePoint(user, point, requestChallengeHistory);

        return requestChallengeHistory;
    }

    // 챌린지 신청 취소
    public ChallengeHistory cancelChallenge(Challenge challenge, User user) {
        if (!isUserRequestingStateChallenge(user, challenge)) {
            throw new IllegalArgumentException(ErrorCode.NO_APPLY.getMessage());
        }

        ChallengeHistory cancelChallengeHistory = new ChallengeHistory(challenge, user, HistoryType.CANCEL);
        challengeHistoryRepository.save(cancelChallengeHistory);

        Long point = challenge.getChallengeSet().getPointAmount();
        pointService.refundPoint(user, point, cancelChallengeHistory);

        return cancelChallengeHistory;
    }

    // 가장 최근 챌린지 신청 기록 조회
    private ChallengeHistory recentChallengeHistory(User user, Challenge challenge) {
        List<ChallengeHistory> challengeHistories = challengeHistoryRepository.findByUserAndChallenge(user, challenge);
        if (challengeHistories.isEmpty()) {
            return null;
        }

        return challengeHistories.get(challengeHistories.size() - 1);
    }

    // 사용자가 챌린지를 신청한 상태인가? (최근 기록이 신청 상태여야함)
    private boolean isUserRequestingStateChallenge(User user, Challenge challenge) {
        ChallengeHistory recentChallengeHistory = recentChallengeHistory(user, challenge);
        // 챌린지 처음 신청하는 경우
        if (recentChallengeHistory == null) {
            return false;
        }
        return HistoryType.REQUEST.equals(recentChallengeHistory.getType());
    }

    // 챌린지 시작
    public ChallengeHistory startChallenge(Challenge challenge, User user) {
        return addChallengeHistory(challenge, user, HistoryType.START);
    }

    // 챌린지 종료
    public ChallengeHistory endChallenge(Challenge challenge, User user) {
        // TODO 과제 완료도에 따라 성공 설패 여부
        return addChallengeHistory(challenge, user, HistoryType.END);
    }

    private ChallengeHistory addChallengeHistory(Challenge challenge, User user, HistoryType type) {
        ChallengeHistory challengeHistory = new ChallengeHistory(challenge, user, type);
        ChallengeHistory savedChallengeHistory = challengeHistoryRepository.save(challengeHistory);
        return savedChallengeHistory;
    }

    // 사용자의 챌린지 조회
    public List<Challenge> getUserChallengeList(User user, ChallengeCategory category) {
        List<ChallengeHistory> allChallengeHistory = challengeHistoryRepository.findAllByUser(user);
        List<Challenge> challengeList = allChallengeHistory.stream()
                .filter(ch -> {
                    return ch.getChallenge().getChallengeSet().getCategory() == category;
                })
                .map(ChallengeHistory::getChallenge)
                .collect(Collectors.toList());
        return challengeList;
    }

    public Map<ChallengeCategory, List<Challenge>> getUserChallengeList(User
                                                                                user, Set<ChallengeCategory> categories) {
        List<ChallengeHistory> allChallengeHistory = challengeHistoryRepository.findAllByUser(user);
        Map<ChallengeCategory, List<Challenge>> challengeMap = new HashMap<>();
        for (ChallengeCategory category : categories) {
            challengeMap.put(category, new ArrayList<>());
        }

        for (ChallengeHistory ch : allChallengeHistory) {
            ChallengeCategory category = ch.getCategory();
            Challenge challenge = ch.getChallenge();
            if (categories.contains(category)) {
                challengeMap.get(category).add(challenge);
            }
        }
        return challengeMap;
    }

    public Map<ChallengeCategory, List<Challenge>> getUserChallengeList(User user, ChallengeCategory... categories) {
        List<ChallengeHistory> allChallengeHistory = challengeHistoryRepository.findAllByUser(user);
        Map<ChallengeCategory, List<Challenge>> challengeMap = new HashMap<>();
        for (ChallengeCategory category : categories) {
            challengeMap.put(category, new ArrayList<>());
        }

        for (ChallengeHistory ch : allChallengeHistory) {
            ChallengeCategory category = ch.getCategory();
            Challenge challenge = ch.getChallenge();
            if (challengeMap.containsKey(category)) {
                challengeMap.get(category).add(challenge);
            }
        }
        return challengeMap;
    }

    public List<Challenge> getUserChallengeList(User user, ChallengeProgressStatus status) {
        List<ChallengeHistory> allChallengeHistory = challengeHistoryRepository.findAllByUser(user);
        List<Challenge> challengeList = allChallengeHistory.stream()
                .filter(challengeHistory -> challengeHistory.getChallenge().getStatus() == status)
                .map(challengeHistory -> challengeHistory.getChallenge())
                .collect(Collectors.toList());
        return challengeList;
    }


    public Challenge getChallenge(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_EXIST.getMessage()));
    }

    public ChallengeHistory getChallengeHistory(Long id) {
        return challengeHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_EXIST.getMessage()));
    }

    public ChallengeSet getChallengeSet(Long id) {
        return challengeSetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_EXIST.getMessage()));
    }

    public Challenge addChallenge(CreateChallengeDto dto) {
        ChallengeSet challengeSet = getChallengeSet(dto.getChallengeSetId());
        LocalDate challengeStartLocalDate = dto.getChallengeStartLocalDate();
        String stage = dto.getStage();

        Challenge challenge = Challenge.makeChallenge(challengeSet, stage, challengeStartLocalDate);
        addChallenge(challenge);

        return challenge;
    }

    public Set<User> findChallengers(List<ChallengeHistory> requestChallenges, Challenge challenge) {
        Set<User> users = requestChallenges.stream()
                .filter(challengeHistory -> challengeHistory.getChallenge().equals(challenge))
                .map(challengeHistory -> challengeHistory.getUser())
                .collect(Collectors.toSet());
        return users;
    }
}
