package com.assignment.auth_service.refresh.infrastructure;

import com.assignment.auth_service.refresh.infrastructure.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query("select r from RefreshTokenEntity r join fetch r.user where r.token = :token")
    Optional<RefreshTokenEntity> findByTokenFetchUser(@Param("token") String token);
}
