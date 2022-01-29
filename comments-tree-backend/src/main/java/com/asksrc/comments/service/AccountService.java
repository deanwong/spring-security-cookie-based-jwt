package com.asksrc.comments.service;

import com.asksrc.comments.dto.AccountDTO;

/**
 * @Author wangding
 * @Date 2022/1/25
 */
public interface AccountService {

    /**
     * handle login request, will be called by UserDetailsService in spring security
     * @see com.asksrc.comments.config.security.AuthUserDetailsServiceImpl
     *
     * @param loginId could be username or email
     * @return account
     * @throws com.asksrc.comments.config.error.EntityNotFoundException if account not found
     */
    AccountDTO login(String loginId);

    /**
     * handle register request
     *
     * @param account must contain username, email, password
     * @return account
     * @throws com.asksrc.comments.config.error.BizException if username or email already exists or invalid
     */
    AccountDTO register(AccountDTO account);

}
