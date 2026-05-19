package com.assignment.auth_service.refresh.infrastructure;

import com.assignment.auth_service.refresh.infrastructure.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query("select r from RefreshTokenEntity r join fetch r.user where r.token = :token")
    Optional<RefreshTokenEntity> findByTokenFetchUser(@Param("token") String token);

    @Query("select r from RefreshTokenEntity r where r.user.id = :userId and r.revoked = false")
    List<RefreshTokenEntity> findActiveByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("update RefreshTokenEntity r set r.revoked = true where r.user.id = :userId and r.revoked = false")
    void revokeAllActiveForUser(@Param("userId") Long userId);
}
