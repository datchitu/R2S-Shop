package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.repository.RoleRepository;
import com.r2s.R2Sshop.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Check exists role by roleName.
     * <p>
     * This function check exists role by roleName, with the roleName as the input parameter.
     * @param roleName
     * @return True if it exists and false if it does not.
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Boolean existsByRoleName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }
}
