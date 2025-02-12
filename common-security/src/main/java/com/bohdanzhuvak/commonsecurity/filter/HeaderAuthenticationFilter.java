package com.bohdanzhuvak.commonsecurity.filter;

import com.bohdanzhuvak.commonsecurity.dto.UserHeaderContext;
import com.bohdanzhuvak.commonsecurity.provider.UserProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter {
  private final UserProvider userProvider;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  )
      throws ServletException, IOException {

    String userId = request.getHeader("X-User-Id");
    String rolesHeader = request.getHeader("X-User-Roles");
    if (userId != null && rolesHeader != null) {
      UserHeaderContext userHeaderContext = new UserHeaderContext(userId, rolesHeader);
      UserDetails user = userProvider.loadUser(userHeaderContext);
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
