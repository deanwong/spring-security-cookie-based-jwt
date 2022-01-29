package com.asksrc.comments.config.security;

import com.asksrc.comments.dto.AccountDTO;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description factory for spring security UserDetails
 */
public final class AuthUserFactory {

    private static final List<GrantedAuthority> authorities = Lists.newArrayList(new SimpleGrantedAuthority("DEFAULT_ROLE"));

    private AuthUserFactory() {
    }

    public static AuthUser create(AccountDTO account) {
        return new AuthUser(
                account,
                authorities
        );
    }

}
