package com.asksrc.comments.config.security;

import com.asksrc.comments.config.error.EntityNotFoundException;
import com.asksrc.comments.dto.AccountDTO;
import com.asksrc.comments.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("authUserDetailService")
@Slf4j
/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description invoke {@link AccountService#login(String)} to get user details
 */
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(final String loginId) throws UsernameNotFoundException {
        AccountDTO accountDTO = null;
        try {
            accountDTO = accountService.login(loginId);
        } catch (EntityNotFoundException ex) {
            throw new UsernameNotFoundException("User Not Found with loginId: " + loginId);
        }
        return AuthUserFactory.create(accountDTO);
    }
}
