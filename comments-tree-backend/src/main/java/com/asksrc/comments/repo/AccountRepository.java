package com.asksrc.comments.repo;

import com.asksrc.comments.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public interface AccountRepository {

    Optional<Account> findById(@Param("id") Long id);

    Optional<Account> findByEmailOrUserName(@Param("loginId") String loginId);

    Optional<Account> findByEmail(@Param("email") String email);

    Optional<Account> findByUsername(@Param("username") String username);

    void create(Account account);
}
