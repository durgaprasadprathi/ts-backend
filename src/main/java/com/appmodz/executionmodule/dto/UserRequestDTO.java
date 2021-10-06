package com.appmodz.executionmodule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class UserRequestDTO extends SearchRequestDTO{
    Long id;
    String userName;
    String firstName;
    String lastName;
    String password;
    Long organizationId;
    Long roleId;
    List<Long> ids;
}
