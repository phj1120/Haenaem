package xyz.parkh.challenge.domain.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import xyz.parkh.challenge.domain.user.entity.AuthKey;
import xyz.parkh.challenge.domain.user.model.MailAuthType;

import java.util.Optional;

public interface AuthKeyRepository extends JpaRepository<AuthKey, Long> {
    Optional<AuthKey> findTopByEmailAndTypeOrderByCreateTimeDesc(String email, MailAuthType type);
}
