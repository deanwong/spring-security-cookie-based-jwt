package com.asksrc.comments.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description Spring filter to check authentication for each request
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("authUserDetailService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // if SecurityContext not in session, then check remember-me token
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authToken = jwtTokenUtil.getJwtFromCookies(request);
            log.debug("request ip [{}] [{} {}] params [{}]", request.getRemoteAddr(), request.getMethod(), request.getRequestURL(), request.getQueryString());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            log.debug("checking authentication for user " + username);

            if (username != null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.debug("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
