package com.appmodz.executionmodule.controller.v1;

import com.appmodz.executionmodule.dto.OrganizationRequestDTO;
import com.appmodz.executionmodule.dto.ResponseDTO;
import com.appmodz.executionmodule.dto.UserRequestDTO;
import com.appmodz.executionmodule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("v1UserController")
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handle(Exception ex, HttpServletRequest request) {
        ResponseDTO responseDTO = new ResponseDTO("failure",ex.getMessage(),null);
        return responseDTO;
    }

    @RequestMapping(value="/",method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object getUsers(@RequestBody(required = false) UserRequestDTO userRequestDTO) {
            return new ResponseDTO("success",null,
                    userService.listUsers());
    }

    @RequestMapping(value="/{id}",method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object getUser(@PathVariable Long id) throws Exception{
        if (id!=null) {
            return new ResponseDTO("success", null,
                    userService.getUserById(id));
        }
        else {
            return new ResponseDTO("failure","Required parameters not present",null);
        }
    }

    @RequestMapping(value="/",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object createAndSearchOrganizations(@RequestBody UserRequestDTO userRequestDTO) throws Exception{
        if(userRequestDTO!=null&&userRequestDTO.getSearch()!=null) {
            return new ResponseDTO("success", null,
                    userService.searchUsers(userRequestDTO));
        } else if (userRequestDTO!=null&&userRequestDTO.getUserName()!=null&& userRequestDTO.getPassword()!=null
                &&userRequestDTO.getOrganizationId()!=null&userRequestDTO.getRoleId()!=null) {
            return new ResponseDTO("success",null,userService.createUser(userRequestDTO));
        } else {
            return new ResponseDTO("failure","Required parameters not present",null);
        }
    }

    @RequestMapping(value="/{id}",method= RequestMethod.PUT, produces="application/json")
    @ResponseBody
    public Object updateUser(@PathVariable Long id,@RequestBody UserRequestDTO userRequestDTO) throws Exception{
            userRequestDTO.setId(id);
            return new ResponseDTO("success",null,
                    userService.updateUser(userRequestDTO));
    }

    @RequestMapping(value="/{id}",method= RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    public Object deleteOrganization(@PathVariable Long id) throws Exception{
        if (id!=null) {
            userService.deleteUser(userService.getUserById(id));
            return new ResponseDTO("success",null, null);
        }
        else {
            return new ResponseDTO("failure","Required parameters not present",null);
        }
    }
}
