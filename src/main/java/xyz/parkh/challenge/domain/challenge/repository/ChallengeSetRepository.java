package xyz.parkh.challenge.domain.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.parkh.challenge.domain.challenge.entity.ChallengeSet;
import xyz.parkh.challenge.domain.challenge.model.ChallengeCategory;

import java.util.List;

public interface ChallengeSetRepository extends JpaRepository<ChallengeSet, Long> {
    List<ChallengeSet> findByCategory(ChallengeCategory challengeCategory);
}
