package org.art.web.json;

import lombok.Data;

@Data
public class CheckLoginResponse {

    private String response;

    public CheckLoginResponse(String response) {
        this.response = response;
    }
}
