package com.appmodz.executionmodule.controller.v1;

import com.appmodz.executionmodule.dto.OrganizationRequestDTO;
import com.appmodz.executionmodule.dto.ResponseDTO;
import com.appmodz.executionmodule.dto.SearchResultDTO;
import com.appmodz.executionmodule.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("v1OrganizationController")
@RequestMapping("/v1/organizations")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handle(Exception ex, HttpServletRequest request) {
        ResponseDTO responseDTO = new ResponseDTO("failure",ex.getMessage(),null);
        return responseDTO;
    }

    @RequestMapping(value="/",method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object getOrganizations(@RequestBody(required = false) OrganizationRequestDTO organizationRequestDTO)
            throws Exception{
        return new ResponseDTO("success",null,
                organizationService.listOrganizations());
    }

    @RequestMapping(value="/{id}",method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object getOrganization(@PathVariable Long id) throws Exception{
        if (id!=null) {
            return new ResponseDTO("success", null,
                    organizationService.getOrganizationById(id));
        }
        else {
            return new ResponseDTO("failure","Required parameters not present",null);
        }
    }

    @RequestMapping(value="/",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object createAndSearchOrganizations(@RequestBody OrganizationRequestDTO organizationRequestDTO) {
        if(organizationRequestDTO!=null&&organizationRequestDTO.getSearch()!=null){
            return new ResponseDTO("success",null,
                    organizationService.searchOrganizations(organizationRequestDTO));
        } else if (organizationRequestDTO.getName()!=null) {
            return new ResponseDTO("success",null,organizationService.createOrganization(organizationRequestDTO.getName()));
        } else {
            return new ResponseDTO("failure","Required parameters not present",null);
        }
    }

    @RequestMapping(value="/{id}",method= RequestMethod.PUT, produces="application/json")
    @ResponseBody
    public Object updateOrganizations(@PathVariable Long id,
                                      @RequestBody OrganizationRequestDTO organizationRequestDTO) {
        if (organizationRequestDTO.getName()!=null&&id!=null) {
            return new ResponseDTO("success",null,
                    organizationService.updateOrganization(id, organizationRequestDTO.getName()));
        } else {
            return new ResponseDTO("failure","Required parameters not present",null);
        }
    }

    @RequestMapping(value="/{id}",method= RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    public Object deleteOrganization(@PathVariable Long id) {
        if (id!=null) {
            organizationService.deleteOrganization(
                    organizationService.getOrganizationById(id));
            return new ResponseDTO("success",null, null);
        }
        else {
            return new ResponseDTO("failure","Required parameters not present",null);
        }
    }
}
