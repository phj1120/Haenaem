package xyz.parkh.challenge.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.parkh.challenge.domain.point.entity.PointHistory;
import xyz.parkh.challenge.domain.user.entity.User;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query("select sum(p.amount) from PointHistory p where p.user=:user")
    Long sumPointAmount(@Param("user") User user);
}
