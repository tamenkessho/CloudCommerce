package com.bohdanzhuvak.commonsecurity.provider;

import com.bohdanzhuvak.commonsecurity.dto.UserHeaderContext;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserProvider {

  UserDetails loadUser(UserHeaderContext userHeaderContext);
}
