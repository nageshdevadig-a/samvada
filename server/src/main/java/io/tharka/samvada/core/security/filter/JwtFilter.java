package io.tharka.samvada.core.security.filter;


import io.tharka.samvada.auth.service.UserDetailsServiceImpl;
import io.tharka.samvada.core.security.service.JWTService;
import io.tharka.samvada.user.model.UserPrincipal;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            if(!request.getMethod().equals("GET")){
                String csrfHeader = request.getHeader("X-Samvada-CSRF");
                if(csrfHeader == null || !csrfHeader.equals("v1")){
                    throw new ServletRequestBindingException("Security Violation: Missing Required header");
                }
            }
            String token = null;
            String userEmail = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("access_token")) {token = cookie.getValue();}
                }
            }
            if(token != null) {
                userEmail = jwtService.extractUserEmail(token);
            }

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(userEmail);

                if (userDetails.isAccountNonExpired() && jwtService.validateToken(userEmail, token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}
