package com.bpa.backend.server.user.account;

import java.util.Map;

public interface LoginService {
    Map<String, String> getToken(String username, String password);
}
