package com.vqd.tme.na2a.task.model;

import lombok.*;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Builder.Default
    private String name = null;
    @Builder.Default
    private String description = null;
    @Builder.Default
    private String creationDate = null;
    @Builder.Default
    private String escalationDate = null;
    @Builder.Default
    private String creationUser = null;
    @Builder.Default
    private String deadline = null;
    @Builder.Default
    private String status = null;
    @Builder.Default
    private Boolean isPack = false;
}
