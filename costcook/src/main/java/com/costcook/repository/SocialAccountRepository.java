package com.costcook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.domain.PlatformTypeEnum;
import com.costcook.entity.SocialAccount;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findBySocialKeyAndProvider(String socialKey, PlatformTypeEnum provider);
}