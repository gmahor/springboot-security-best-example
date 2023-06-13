package com.core.constant;


public class MessageConstant {

    private MessageConstant() {
        throw new IllegalArgumentException("MessageConstant is a utility class");
    }

    public static final String TOKEN_ID_EXPIRED = "token id was expired. Please make a new signin request";
    public static final String REFRESH_TOKEN_NOT_FOUND_WITH_THIS_TOKEN_ID = "Refresh token was not found with this tokenId";
    public static final String REFRESH_TOKEN_GENERATE_SUCCESSFULLY = "Refresh token was generated successfully";


}
