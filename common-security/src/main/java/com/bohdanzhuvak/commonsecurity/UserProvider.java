package com.bohdanzhuvak.commonsecurity;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserProvider {
  UserDetails loadUser(UserHeaderContext userHeaderContext);
}
