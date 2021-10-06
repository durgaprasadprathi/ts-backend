package com.appmodz.executionmodule.controller.v1;

import com.appmodz.executionmodule.dto.CanvasRequestDTO;
import com.appmodz.executionmodule.dto.FileOrFolder;
import com.appmodz.executionmodule.dto.OrganizationRequestDTO;
import com.appmodz.executionmodule.dto.ResponseDTO;
import com.appmodz.executionmodule.service.CanvasService;
import com.appmodz.executionmodule.service.StackService;
import com.appmodz.executionmodule.service.TerraformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("v1CanvasController")
@RequestMapping("/v1/canvas")
public class CanvasController {

    @Autowired
    CanvasService canvasService;

    @Autowired
    StackService stackService;

    @Autowired
    TerraformService terraformService;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handle(Exception ex, HttpServletRequest request) {
        ResponseDTO responseDTO = new ResponseDTO("failure",ex.getMessage(),null);
        return responseDTO;
    }

    @RequestMapping(value="/components",method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object getComponents() throws Exception{
        return new ResponseDTO("success",null,
                canvasService.listComponents());
    }


    @RequestMapping(value="/code/folder",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object getFolderStructure(@RequestBody(required = false) CanvasRequestDTO canvasRequestDTO) throws Exception{
        String path = ""+canvasRequestDTO.getWorkspaceId()+canvasRequestDTO.getPath();
        return new ResponseDTO("success",null,
                canvasService.getFolderStructure(path));
    }

    @RequestMapping(value="/code/file",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object getFileContents(@RequestBody(required = false) CanvasRequestDTO canvasRequestDTO) throws Exception{
        String path = ""+canvasRequestDTO.getWorkspaceId()+canvasRequestDTO.getPath();
        return new ResponseDTO("success",null,
                canvasService.getFileContent(path));
    }

    @RequestMapping(value="/save",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object saveDraftState(@RequestBody(required = false) CanvasRequestDTO canvasRequestDTO) throws Exception{
        if(canvasRequestDTO.getIsDraft() == null || canvasRequestDTO.getIsDraft())
        return new ResponseDTO("success",null,
                stackService.saveState(canvasRequestDTO));
        else
            return new ResponseDTO("success",null,
                    terraformService.terraformStateWiseMovement(canvasRequestDTO.getWorkspaceId()));

    }

    @RequestMapping(value="/validate",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object validate(@RequestBody(required = false) CanvasRequestDTO canvasRequestDTO) throws Exception{
            return new ResponseDTO("success",null,
                    terraformService.terraformValidate(canvasRequestDTO.getWorkspaceId()));

    }

    @RequestMapping(value="/plan",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object plan(@RequestBody(required = false) CanvasRequestDTO canvasRequestDTO) throws Exception{
        return new ResponseDTO("success",null,
                terraformService.terraformPlan(canvasRequestDTO.getWorkspaceId()));

    }

    @RequestMapping(value="/publish",method= RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Object publish(@RequestBody(required = false) CanvasRequestDTO canvasRequestDTO) throws Exception{
        return new ResponseDTO("success",null,
                terraformService.terraformApply(canvasRequestDTO.getWorkspaceId()));

    }

    @RequestMapping(value="/component/{id}",method= RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object geComponentProperties(@PathVariable Long id)
            throws Exception{
        if(id==1) {
            return new ResponseDTO("success",null,
                    "[\n" +
                            "  {\"id\":1,\"name\":\"vpc_cidr\",\"default_value\":\"\",\"type\":\"string\"},\n" +
                            "  {\"id\":2,\"name\":\"tenancy\",\"default_value\":\"default\",\"type\":\"string\"},\n" +
                            "  {\"id\":3,\"name\":\"product\",\"default_value\":\"\",\"type\":\"string\"},\n" +
                            "  {\"id\":4,\"name\":\"environment\",\"default_value\":\"\",\"type\":\"string\"}\n" +
                            "]");
        } else if(id==2) {
            return new ResponseDTO("success",null,
                    "[\n" +
                            "  {\"id\":5,\"name\":\"public_subnets\",\"default_value\":\"\",\"type\":\"string\"},\n" +
                            "  {\"id\":6,\"name\":\"vpc_id\",\"default_value\":\"\",\"type\":\"dropdown\"},\n" +
                            "  {\"id\":7,\"name\":\"availability_zones\",\"default_value\":\"\",\"type\":\"string\"}\n" +
                            "]");
        } else {
            return new ResponseDTO("failure","component with this id not found",null);
        }
    }
}
