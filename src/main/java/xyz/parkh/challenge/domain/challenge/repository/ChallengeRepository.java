package xyz.parkh.challenge.domain.challenge.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.parkh.challenge.domain.challenge.entity.Challenge;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    @EntityGraph(attributePaths = {"challengeSet"})
    List<Challenge> findAll();

    @Query("SELECT c " +
            "FROM Challenge c JOIN FETCH c.challengeSet cs " +
            "WHERE c.id =:id")
    Optional<Challenge> findById(@Param("id") Long id);

    @Query("SELECT c " +
            "FROM Challenge c JOIN FETCH c.challengeSet " +
            "where c.challengeSet.category = :category")
    List<Challenge> findByCategory(@Param("category") ChallengeCategory category);

    @Query("SELECT c " +
            "FROM Challenge c JOIN FETCH c.challengeSet cs " +
            "WHERE c.challengeStartLocalDate = :startLocalDate")
    List<Challenge> findByChallengeStartLocalDate(@Param("startLocalDate") LocalDate startLocalDate);

    @Query("SELECT c " +
            "FROM Challenge c JOIN FETCH c.challengeSet cs " +
            "WHERE c.challengeEndLocalDate = :endLocalDate")
    List<Challenge> findByChallengeEndLocalDate(@Param("endLocalDate") LocalDate endLocalDate);


}
