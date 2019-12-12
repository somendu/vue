package com.vqd.tme.na2a.model.tme;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TmeLoginCredentials {
    private String username;
    private String password;
}
