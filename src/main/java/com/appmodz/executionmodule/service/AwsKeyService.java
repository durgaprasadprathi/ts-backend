package com.appmodz.executionmodule.service;

import com.appmodz.executionmodule.dao.AwsKeyDAO;
import com.appmodz.executionmodule.dao.StackDAO;
import com.appmodz.executionmodule.dto.AwsKeyRequestDTO;
import com.appmodz.executionmodule.model.AwsKey;
import com.appmodz.executionmodule.model.Stack;
import com.appmodz.executionmodule.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AwsKeyService {
    @Autowired
    AwsKeyDAO awsKeyDAO;

    @Autowired
    StackDAO stackDAO;

    @Autowired
    StackService stackService;

    public AwsKey getByStackId(long stackId) throws Exception{
        AwsKey awsKey = awsKeyDAO.getByStackId(stackId);
        if (awsKey==null)
            throw new Exception("Aws Credentials Not Found For this stack");
        return awsKey;
    }

    public AwsKey createAwsKey(AwsKeyRequestDTO awsKeyRequestDTO, User user) throws Exception{
        AwsKey findAwsKey = awsKeyDAO.getByStackId(awsKeyRequestDTO.getStackId());
        if(findAwsKey!=null) {
            throw new Exception("This workspace already has an awsKey associated with it");
        }
        AwsKey awsKey = new AwsKey();
        awsKey.setAwsAccessKey(awsKeyRequestDTO.getAwsAccessKey());
        awsKey.setAwsSecretAccessKey(awsKeyRequestDTO.getAwsSecretAccessKey());
        awsKey.setAwsRegion(awsKeyRequestDTO.getAwsRegion());
        awsKey.setUser(user);
        Stack stack = stackService.getStackById(awsKeyRequestDTO.getStackId());
        List<Stack> list= new ArrayList<>();
        list.add(stack);
        awsKey.setStacks(list);
        awsKeyDAO.save(awsKey);
        return awsKey;
    }

    public void deleteAwsKey(AwsKey awsKey) {
        awsKeyDAO.delete(awsKey);
    }

    public AwsKey updateAwsKey(AwsKeyRequestDTO awsKeyRequestDTO, User user) throws Exception{
        AwsKey awsKey = awsKeyDAO.get(awsKeyRequestDTO.getId());
        List<Stack> list = awsKey.getStacks();
        boolean found = false;
        for (Stack li:list) {
            if(li.getStackId()==awsKeyRequestDTO.getStackId()) {
                found = true;
                break;
            }
        }
        if(!found)
            throw new Exception("This aws key is not associated with this stack");
        awsKey.setUser(user);
        if(awsKeyRequestDTO.getAwsAccessKey() != null) {
            awsKey.setAwsAccessKey(awsKeyRequestDTO.getAwsAccessKey());
        }
        if(awsKeyRequestDTO.getAwsRegion() != null) {
            awsKey.setAwsRegion(awsKeyRequestDTO.getAwsRegion());
        }
        if(awsKeyRequestDTO.getAwsSecretAccessKey() != null) {
            awsKey.setAwsSecretAccessKey(awsKeyRequestDTO.getAwsSecretAccessKey());
        }
        if(awsKeyRequestDTO.getStackId()!=0) {
            Stack stack = stackService.getStackById(awsKeyRequestDTO.getStackId());
            list.add(stack);
            awsKey.setStacks(list);
        }
        awsKeyDAO.save(awsKey);
        return awsKey;
    }
}
