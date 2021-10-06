package com.appmodz.executionmodule.dto;

import java.util.Objects;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class CanvasRequestDTO {
    long workspaceId;
    String path;
    Object draftState;
    Boolean isDraft;

    public CanvasRequestDTO() {
        this.path = Objects.requireNonNullElse(path, "");
    }
}
