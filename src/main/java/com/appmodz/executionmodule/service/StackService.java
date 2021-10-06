package com.appmodz.executionmodule.service;

import com.appmodz.executionmodule.dao.StackDAO;
import com.appmodz.executionmodule.dao.TerraformBackendDAO;
import com.appmodz.executionmodule.dto.CanvasRequestDTO;
import com.appmodz.executionmodule.dto.SearchRequestDTO;
import com.appmodz.executionmodule.dto.SearchResultDTO;
import com.appmodz.executionmodule.dto.StackRequestDTO;
import com.appmodz.executionmodule.model.Organization;
import com.appmodz.executionmodule.model.Stack;
import com.appmodz.executionmodule.model.TerraformBackend;
import com.appmodz.executionmodule.model.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class StackService {

    @Autowired
    StackDAO stackDAO;

    @Autowired
    UserService userService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    TerraformBackendDAO terraformBackendDAO;


    @Autowired
    private Environment env;

    String working_dir = env.getProperty("WORKING_DIR");

    public Stack getStackById(long stackId) throws Exception{
        Stack stack = stackDAO.get(stackId);
        if(stack==null)
            throw new Exception("No stack with this stack id exists");
        else
            return stack;
    }

    public Stack saveState(CanvasRequestDTO canvasRequestDTO) throws Exception{
        Stack stack = this.getStackById(canvasRequestDTO.getWorkspaceId());
        stack.setStackDraftState(canvasRequestDTO.getDraftState());
        stackDAO.save(stack);
        return stack;
    }

    public Stack createStack(StackRequestDTO stackRequestDTO) throws Exception{
        Stack stack = new Stack();
        User user = userService.getUserById(stackRequestDTO.getOwnerId());
        stack.setOwner(user);
        TerraformBackend terraformBackend = new TerraformBackend();
        terraformBackend.setName(stackRequestDTO.getName());
        terraformBackendDAO.save(terraformBackend);
        stack.setTerraformBackend(terraformBackend);

        stack.setAwsAccessKey(stackRequestDTO.getAwsAccessKey());
        stack.setAwsRegion(stackRequestDTO.getAwsRegion());
        stack.setAwsSecretAccessKey(stackRequestDTO.getAwsSecretAccessKey());

        Organization organization = organizationService.getOrganizationById(stackRequestDTO.getOrganizationId());
        stack.setOrganization(organization);


        stackDAO.save(stack);

        File file = new File(working_dir+stack.getStackId());
        if (!file.exists()) {
            if (file.mkdir()) {

                File source = new File(working_dir+"basic_template");
                File dest = new File(working_dir+stack.getStackId());
                try {
                    FileUtils.copyDirectory(source, dest);
                    stack.setStackLocation(working_dir+stack.getStackId());
                    stackDAO.save(stack);
                    return stack;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("Error in folder creation");
                }
            } else {
               throw new Exception("Error in folder creation");
            }
        } else {
            throw new Exception("Already Exists");
        }
    }

    public Stack editStack(StackRequestDTO stackRequestDTO) throws Exception{
        Stack stack = stackDAO.get(stackRequestDTO.getId());
        if(stackRequestDTO.getOwnerId()!=null&&stackRequestDTO.getOrganizationId()!=null) {
            User user = userService.getUserById(stackRequestDTO.getOwnerId());
            Organization organization = organizationService.getOrganizationById(stackRequestDTO.getOrganizationId());
            if (user.getUserOrganization().getOrganizationId()!=organization.getOrganizationId()) {
                throw new Exception("Stack owner belongs to different organization");
            }
        }
        else if(stackRequestDTO.getOwnerId()!=null) {
            User user = userService.getUserById(stackRequestDTO.getOwnerId());
            if (user.getUserOrganization().getOrganizationId()!=stack.getOrganization().getOrganizationId()) {
                throw new Exception("Stack owner belongs to different organization");
            }
            stack.setOwner(user);
        }
        else if(stackRequestDTO.getOrganizationId()!=null) {
            Organization organization = organizationService.getOrganizationById(stackRequestDTO.getOrganizationId());
            if (stack.getOwner().getUserOrganization().getOrganizationId()!=stackRequestDTO.getOrganizationId()) {
                throw new Exception("Stack owner belongs to different organization");
            }
            stack.setOrganization(organization);
        }
        if (stackRequestDTO.getTerraformBackendId()!=null){
            TerraformBackend terraformBackend = terraformBackendDAO.get(stackRequestDTO.getTerraformBackendId());
            if(stackRequestDTO.getName()!=null) {
                terraformBackend.setName(stackRequestDTO.getName());
                terraformBackendDAO.save(terraformBackend);
            }

            stack.setTerraformBackend(terraformBackend);
        }
        if(stackRequestDTO.getName()!=null) {
            TerraformBackend terraformBackend = stack.getTerraformBackend();
            terraformBackend.setName(stackRequestDTO.getName());
            terraformBackendDAO.save(terraformBackend);
        }
        if(stackRequestDTO.getAwsAccessKey()!=null)
        stack.setAwsAccessKey(stackRequestDTO.getAwsAccessKey());
        if(stackRequestDTO.getAwsRegion()!=null)
        stack.setAwsRegion(stackRequestDTO.getAwsRegion());
        if(stackRequestDTO.getAwsSecretAccessKey()!=null)
        stack.setAwsSecretAccessKey(stackRequestDTO.getAwsSecretAccessKey());

        stackDAO.save(stack);
        return stack;
    }


    public void deleteStack(Stack stack) {
        stackDAO.delete(stack);
    }

    public List listStacks() {
        return stackDAO.getAll();
    }

    public SearchResultDTO searchStacks(SearchRequestDTO searchRequestDTO) {
        return stackDAO.search(searchRequestDTO);
    }
}
