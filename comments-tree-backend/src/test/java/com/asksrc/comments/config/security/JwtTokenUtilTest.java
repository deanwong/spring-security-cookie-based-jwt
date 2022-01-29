package com.asksrc.comments.config.security;

import com.asksrc.comments.BaseTestSuite;
import com.asksrc.comments.dto.AccountDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author wangding
 * @Date 2022/1/26
 */
public class JwtTokenUtilTest extends BaseTestSuite {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void TestGenerateJwtCookie() {
        AccountDTO accountDTO = new AccountDTO();
        // foobar
        accountDTO.setPassword("$2a$10$HDpzNlRgsMiL75Vvd3U2HeXiXPHQBjDDw2A2fY4/DD6UEF5hxCrQq");
        accountDTO.setEmail("foo@bar.com");
        accountDTO.setStatus(1);
        accountDTO.setId(1L);
        accountDTO.setUsername("foobar");
        AuthUser user = AuthUserFactory.create(accountDTO);
        ResponseCookie cookie = jwtTokenUtil.generateJwtCookie(user);
        assertSame(cookie.getPath(), "/");
        assertEquals(2592000L, cookie.getMaxAge().getSeconds());
        String token = cookie.getValue();
        assertTrue(jwtTokenUtil.validateToken(token, user));
        Date exp = jwtTokenUtil.getExpirationDateFromToken(token);
        // cookie expire time should be same as token expire time
        assertTrue(Math.abs(exp.toInstant().getEpochSecond() * 1000 - (System.currentTimeMillis() + cookie.getMaxAge().toMillis())) < 10000);
    }
}
