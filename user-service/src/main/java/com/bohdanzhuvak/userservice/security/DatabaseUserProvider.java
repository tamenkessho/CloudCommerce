package com.bohdanzhuvak.userservice.security;

import com.bohdanzhuvak.commonsecurity.UserHeaderContext;
import com.bohdanzhuvak.commonsecurity.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseUserProvider implements UserProvider {
  private final UserDetailsService userDetailsService;
  @Override
  public UserDetails loadUser(UserHeaderContext userHeaderContext) {
    return userDetailsService.loadUserByUsername(userHeaderContext.userId());
  }
}
