package org.example.service;

import org.example.dao.AuthRepository;

public class AuthService {
    private final AuthRepository authRepository;

    public AuthService() {
        authRepository = new AuthRepository();
    }
}
