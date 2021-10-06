package com.appmodz.executionmodule.dto;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class AwsKeyRequestDTO extends SearchRequestDTO{
    long id;

    String awsAccessKey;

    String awsSecretAccessKey;

    String awsRegion;

    long stackId;
}
