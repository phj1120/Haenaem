package xyz.parkh.challenge.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.parkh.challenge.domain.task.entity.Task;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByChallengeHistoryId(long challengeHistoryId);
//    List<Task> findByChallengeId(long challengeId);

//    List<Task> findByChallengeId(long challengeId);


    @Query("select t from Task t join fetch t.challengeHistory ch " +
            "join fetch ch.challenge c " +
            "where c.id = :challengeId")
    List<Task> findByChallengeId(@Param("challengeId") Long challengeId);

//    @Query("select t from Task  t " +
//            "where t.challengeHistory.id = :challengeHistoryId")
//    List<Task> findByChallengeHistoryId(@Param("challengeHistoryId") long challengeHistoryId);
}

