package xyz.parkh.challenge.domain.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeHistory;
import xyz.parkh.challenge.domain.challenge.model.HistoryType;
import xyz.parkh.challenge.domain.user.entity.User;

import java.util.List;

public interface ChallengeHistoryRepository extends JpaRepository<ChallengeHistory, Long> {
    List<ChallengeHistory> findAllByChallenge(Challenge challenge);

    @Query("select ch from ChallengeHistory  ch " +
            "join fetch ch.challenge c " +
            "join fetch c.challengeSet cs " +
            "where ch.user = :user")
    List<ChallengeHistory> findAllByUser(@Param("user") User user);

    @Query("select ch from ChallengeHistory ch " +
            "join fetch ch.challenge c " +
            "join fetch c.challengeSet cs " +
            "join fetch ch.user u " +
            "where ch.type = :type")
    List<ChallengeHistory> findAllByType(@Param("type") HistoryType type);

    List<ChallengeHistory> findByUserAndChallenge(User user, Challenge challenge);


//    List<ChallengeHistory> findAllByUser(User user);

//    @Query("select ch from ChallengeHistory  ch " +
//            "where ch.user=:user")
//    List<ChallengeHistory> findAllByUser(@Param("user") User user);

//    @Query("select ch from ChallengeHistory  ch " +
//            "join fetch Challenge  c on ch.challenge.id = c.id " +
//            "join fetch ChallengeSet cs on ch.challenge.challengeSet.id = cs.id " +
//            "where ch.user = :user")
//    List<ChallengeHistory> findAllByUser(@Param("user") User user);

// user 은 이미 영속성 컨텍스트에 있기 때문에 중복으로 가져올 필요 없다고 생각
//    @Query("select ch from ChallengeHistory  ch " +
//            "join fetch ch.challenge c " +
//            "join fetch c.challengeSet cs " +
//            "join fetch ch.user u " +
//            "where ch.user = :user")
//    List<ChallengeHistory> findAllByUser(@Param("user") User user);

}
