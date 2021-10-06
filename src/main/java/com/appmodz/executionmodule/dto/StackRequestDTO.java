package com.appmodz.executionmodule.dto;

import java.util.List;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class StackRequestDTO extends SearchRequestDTO{
    Long id;
    Long terraformBackendId;
    Long organizationId;
    Long ownerId;
    String stackState;
    String name;
    String stackLocation;
    String awsAccessKey;
    String awsSecretAccessKey;
    String awsRegion;
    List<Long> ids;
}
