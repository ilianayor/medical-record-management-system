package com.example.medical.record.service;

import com.example.medical.record.domain.entity.Role;

public interface RoleService {
    Role getRole(String authority);
}
