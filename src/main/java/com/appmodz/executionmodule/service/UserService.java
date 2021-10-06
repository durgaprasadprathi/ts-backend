package com.appmodz.executionmodule.service;

import com.appmodz.executionmodule.dao.OrganizationDAO;
import com.appmodz.executionmodule.dao.RoleDAO;
import com.appmodz.executionmodule.dao.UserDAO;
import com.appmodz.executionmodule.dto.SearchResultDTO;
import com.appmodz.executionmodule.dto.SearchRequestDTO;
import com.appmodz.executionmodule.dto.UserRequestDTO;
import com.appmodz.executionmodule.model.Organization;
import com.appmodz.executionmodule.model.Role;
import com.appmodz.executionmodule.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    RoleDAO roleDAO;

    public User getUserById(long userId) throws Exception{
        User user = userDAO.get(userId);
        if(user==null)
            throw new Exception("No user with this user id exists");
        else
            return user;
    }

    public User getUserByUsername(String userName) throws Exception{
        User user = userDAO.getByUsername(userName);
        if(user==null)
            throw new Exception("No user with this user id exists");
        else
            return user;
    }

    public User createUser(UserRequestDTO userRequestDTO) throws Exception{
        User user = new User();
        user.setUserFirstName(userRequestDTO.getFirstName());
        user.setUserLastName(userRequestDTO.getLastName());
        user.setUserName(userRequestDTO.getUserName());
        User checkUser = userDAO.getByUsername(userRequestDTO.getUserName());
        if (checkUser!=null)
            throw new Exception("User with this username already exists");
        user.setUserPasswordHash(userRequestDTO.getPassword());
        Organization organization = organizationDAO.get(userRequestDTO.getOrganizationId());
        if (organization==null)
            throw new Exception("Organization with this id not found");
        Role role = roleDAO.get(userRequestDTO.getRoleId());
        if (role==null)
            throw new Exception("Role with this id not found");
        user.setUserOrganization(organization);
        user.setUserRole(role);
        userDAO.save(user);
        return user;
    }

    public User updateUser(UserRequestDTO userRequestDTO) throws Exception{
        User user = userDAO.get(userRequestDTO.getId());
        if(userRequestDTO.getFirstName()!=null)
        user.setUserFirstName(userRequestDTO.getFirstName());
        if(userRequestDTO.getLastName()!=null)
        user.setUserLastName(userRequestDTO.getLastName());
        if(userRequestDTO.getUserName()!=null) {
            User checkUser = userDAO.getByUsername(userRequestDTO.getUserName());
            if (checkUser != null)
                throw new Exception("User with this username already exists");
            user.setUserName(userRequestDTO.getUserName());
        }
        if(userRequestDTO.getPassword()!=null)
        user.setUserPasswordHash(userRequestDTO.getPassword());
        if(userRequestDTO.getOrganizationId()!=null) {
            Organization organization = organizationDAO.get(userRequestDTO.getOrganizationId());
            if (organization == null)
                throw new Exception("Organization with this id not found");
            user.setUserOrganization(organization);
        }
        if(userRequestDTO.getRoleId()!=null) {
            Role role = roleDAO.get(userRequestDTO.getRoleId());
            if (role == null)
                throw new Exception("Role with this id not found");
            user.setUserRole(role);
        }
        userDAO.save(user);
        return user;
    }

    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    public List listUsers() {
        return userDAO.getAll();
    }

    public SearchResultDTO searchUsers(SearchRequestDTO searchRequestDTO) {
        return userDAO.search(searchRequestDTO);
    }

}
