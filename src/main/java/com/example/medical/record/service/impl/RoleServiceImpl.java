package com.example.medical.record.service.impl;

import com.example.medical.record.domain.entity.Role;
import com.example.medical.record.repository.RoleRepository;
import com.example.medical.record.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRole(String authority) {
        return roleRepository.findByAuthority(authority);
    }
}
