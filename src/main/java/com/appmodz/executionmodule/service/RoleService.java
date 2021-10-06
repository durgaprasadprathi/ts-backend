package com.appmodz.executionmodule.service;

import com.appmodz.executionmodule.dao.PermissionDAO;
import com.appmodz.executionmodule.dao.RoleDAO;
import com.appmodz.executionmodule.dao.RolePermissionDAO;
import com.appmodz.executionmodule.model.Permission;
import com.appmodz.executionmodule.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleDAO roleDAO;

    @Autowired
    RolePermissionDAO rolePermissionDAO;

    @Autowired
    PermissionDAO permissionDAO;

    public Role getRoleById(long roleId) {
        return roleDAO.get(roleId);
    }

}
