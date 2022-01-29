package com.asksrc.comments.config.security;

import com.asksrc.comments.dto.AccountDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description wrapper for account
 */
public class AuthUser implements UserDetails {

    private final Collection<? extends GrantedAuthority> authorities;
    private final AccountDTO account;

    public AuthUser(
            AccountDTO account,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.account = account;
        this.authorities = authorities;
    }

    public AccountDTO getAccount() {
        return account;
    }

    @Override
    public String getUsername() {
        return this.account.getUsername();
    }


    @Override
    public String getPassword() {
        return this.account.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return this.account.getStatus() == 1;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
