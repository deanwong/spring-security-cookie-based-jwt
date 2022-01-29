package com.asksrc.comments.web;

import com.asksrc.comments.config.error.BizException;
import com.asksrc.comments.config.error.ExceptionCodeEnum;
import com.asksrc.comments.config.security.AuthUser;
import com.asksrc.comments.config.security.JwtTokenUtil;
import com.asksrc.comments.dto.AccountDTO;
import com.asksrc.comments.dto.payload.LoginRequest;
import com.asksrc.comments.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api/auth")
@Slf4j
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info(">>> request login with id [{}]", loginRequest.getLoginId());
        Authentication authentication =null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            log.warn("authentication fail with id [{}]", loginRequest.getLoginId());
            throw new BizException(ExceptionCodeEnum.BAD_CREDENTIAL);
        }
        log.info("authentication success with id [{}]", loginRequest.getLoginId());
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        AccountDTO accountDTO = authUser.getAccount();
        // for secure not expose pwd
        accountDTO.setPassword(null);
        // set to session
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        if (loginRequest.isRememberMe()) {
            // generate cookie
            ResponseCookie jwtCookie = jwtTokenUtil.generateJwtCookie(authUser);
            log.info("enable remember-me token for [{}]", authUser.getUsername());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(accountDTO);
        } else {
            return ResponseEntity.ok().body(accountDTO);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AccountDTO account) {
        log.info(">>> request register with username [{}]", account.getUsername());
        AccountDTO savedAccount = accountService.register(account);
        return ResponseEntity.ok(savedAccount);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        log.info(">>> request auth ping from frontend");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO accountDTO = null;
        if (auth.getPrincipal() instanceof AuthUser) {
            AuthUser authUser = (AuthUser) auth.getPrincipal();
            accountDTO = authUser.getAccount();
            accountDTO.setPassword(null);
        }
        if (accountDTO == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return ResponseEntity.ok(accountDTO);
    }

}
