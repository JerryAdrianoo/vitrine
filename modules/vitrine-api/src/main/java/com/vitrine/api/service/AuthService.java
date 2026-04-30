package com.vitrine.api.service;

import com.vitrine.api.model.Customer;

public interface AuthService {
    Customer authenticate(String email, String password);
}
