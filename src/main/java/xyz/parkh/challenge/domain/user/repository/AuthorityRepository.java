package xyz.parkh.challenge.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.parkh.challenge.domain.user.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {


}
