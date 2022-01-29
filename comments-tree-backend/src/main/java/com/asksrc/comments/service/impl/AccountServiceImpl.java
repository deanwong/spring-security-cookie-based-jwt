package com.asksrc.comments.service.impl;

import com.asksrc.comments.config.error.BizException;
import com.asksrc.comments.config.error.EntityNotFoundException;
import com.asksrc.comments.config.error.ExceptionCodeEnum;
import com.asksrc.comments.dto.AccountDTO;
import com.asksrc.comments.entity.Account;
import com.asksrc.comments.mapper.AccountMapper;
import com.asksrc.comments.repo.AccountRepository;
import com.asksrc.comments.service.AccountService;
import com.asksrc.comments.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountDTO login(String loginId) {
        Account account = accountRepository.findByEmailOrUserName(loginId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        AccountDTO accountDTO = accountMapper.toDTO(account);
        accountDTO.setPassword(account.getPassword());
        return accountDTO;
    }

    @Override
    public AccountDTO register(AccountDTO account) {
        this.validateAccount(account);
        Account entity = accountMapper.toEntity(account);
        entity.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.create(entity);
        return accountMapper.toDTO(entity);
    }

    private void validateAccount(AccountDTO account) {
        if (!Validator.isValidUsername(account.getUsername())) {
            throw new BizException(ExceptionCodeEnum.INVALID_USERNAME);
        }
        if (!Validator.isValidPwd(account.getPassword())) {
            throw new BizException(ExceptionCodeEnum.INVALID_PASSWORD);
        }
        if (!Validator.isValidEmail(account.getEmail())) {
            throw new BizException(ExceptionCodeEnum.INVALID_EMAIL);
        }
        // check username if exist
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new BizException(ExceptionCodeEnum.EXIST_USERNAME);
        }
        // check email if exist
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new BizException(ExceptionCodeEnum.EXIST_EMAIL);
        }
    }

}
