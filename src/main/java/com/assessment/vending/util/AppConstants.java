package com.assessment.vending.util;

public class AppConstants {

    public static final String API_VERSION_ONE = "/api/v1";
    public static final String USERS = API_VERSION_ONE + "/users";
    public static final String PRODUCTS = API_VERSION_ONE + "/products";
    public static final String ONE_PRODUCT_URL = PRODUCTS + "/{productID}";

    public static final String DEPOSIT = API_VERSION_ONE + "/deposit";
    public static final String BUY = API_VERSION_ONE + "/buy";
    public static final String RESET = API_VERSION_ONE + "/reset";
    public static final String SESSION = API_VERSION_ONE + "/sessions";

    public static final String ROLE_BUYER = "buyer";
    public static final String ROLE_SELLER = "seller";

    public static final String EM_SAVING_TO_DATABASE = "Error Occurred while saving to the database \n";

}
