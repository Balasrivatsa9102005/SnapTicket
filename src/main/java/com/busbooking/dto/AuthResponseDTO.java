
package com.busbooking.dto;

public class AuthResponseDTO {

    private String token;
    private String tokenType;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}
