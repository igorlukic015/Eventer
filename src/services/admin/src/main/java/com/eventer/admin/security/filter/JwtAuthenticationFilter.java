package com.eventer.admin.security.filter;

import com.eventer.admin.security.contracts.AuthorityConstants;
import com.eventer.admin.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String authorizationHeader = "Authorization";
    private final String jwtPrefix = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${user-service-url}")
    private String userServiceUrl;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String requestAddress = request.getRemoteAddr();

        logger.info(String.format("Authentication attempt from %s", requestAddress));

        String authHeader = request.getHeader(this.authorizationHeader);

        if (authHeader == null || !authHeader.startsWith(this.jwtPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(this.jwtPrefix.length());

        boolean isService = requestAddress.equals(this.userServiceUrl);

        String username = this.jwtService.extractUsername(token, isService);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (username.equals(AuthorityConstants.USER_SERVICE)) {
                if (this.jwtService.validateTokenFromService(token)) {
                    logger.info("Service token validated. Requested {}", request.getPathInfo());

                    AbstractAuthenticationToken authToken =
                            new AnonymousAuthenticationToken(
                                    AuthorityConstants.USER_SERVICE,
                                    AuthorityConstants.USER_SERVICE,
                                    List.of(
                                            new SimpleGrantedAuthority(
                                                    AuthorityConstants.USER_SERVICE)));

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.jwtService.validateToken(token, userDetails)) {
                logger.info("User {} validated", username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
