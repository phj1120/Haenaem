package xyz.parkh.challenge.domain.point.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.exception.ErrorCode;
import xyz.parkh.challenge.domain.point.entity.PointHistory;
import xyz.parkh.challenge.domain.point.repository.PointHistoryRepository;
import xyz.parkh.challenge.domain.user.entity.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PointService {
    private final PointHistoryRepository pointHistoryRepository;

    public Long getPointAmount(User user) {
        Long sumPointAmount = pointHistoryRepository.sumPointAmount(user);
        if (sumPointAmount == null) {
            return 0L;
        }
        return sumPointAmount;
    }

    public PointHistory chargePoint(User user, Long amount) {
        PointHistory pointHistory = user.chargePoint(amount);

        return pointHistory;
    }

    public PointHistory refundPoint(User user, Long amount, ChallengeHistory cancelChallengeHistory) {
        PointHistory pointHistory = user.refundPoint(amount, cancelChallengeHistory);

        return pointHistory;
    }

    public PointHistory usePoint(User user, Long amount, ChallengeHistory challengeHistory) {
        Long totalPoint = getPointAmount(user);
        if (totalPoint < amount) {
            throw new IllegalArgumentException(ErrorCode.INSUFFICIENT_POINT.getMessage());
        }

        PointHistory pointHistory = user.usePoint(amount, challengeHistory);

        return pointHistory;
    }
}
