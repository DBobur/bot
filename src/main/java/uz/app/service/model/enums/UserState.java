package uz.app.service.model.enums;

public enum UserState {
    // for full users
    REGISTERED,
    // for client users
    GET_PRODUCTS,
    GET_ORDERS,
    GET_ORDER,
    GET_HISTORIES,
    // for admin users
    ADD_PRODUCT,
    ADD_CATEGORY,
    MANAGE_ORDERS,
    GET_STATISTICS,
    MANAGE_CP,
}
