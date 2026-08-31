package org.example.bankappuserservice.infrastructure.adapters.in.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bankappuserservice.application.ports.out.TokenBlackListPort;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenBlackListFilter extends OncePerRequestFilter {
    private final TokenBlackListPort tokenBlackListPort;

    public TokenBlackListFilter(TokenBlackListPort tokenBlackListPort) {
        this.tokenBlackListPort = tokenBlackListPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7).trim();
            if(tokenBlackListPort.isRevoked(token)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return "POST".equalsIgnoreCase(request.getMethod()) && "/auth".equals(request.getServletPath());
    }
}
