package ui.screens.common;

import lombok.Getter;

@Getter
public enum Screens {

    CUSTOMER_LIST_SCREEN(ScreenConstants.FXML_ROUTE_CUSTOMER_LIST_SCREEN),
    CUSTOMER_ADD_SCREEN(ScreenConstants.FXML_ROUTE_CUSTOMER_ADD_SCREEN),
    CUSTOMER_UPDATE_SCREEN(ScreenConstants.FXML_ROUTE_CUSTOMER_UPDATE_SCREEN),
    CUSTOMER_DELETE_SCREEN(ScreenConstants.FXML_ROUTE_CUSTOMER_DELETE_SCREEN),
    WELCOME_SCREEN(ScreenConstants.FXML_ROUTE_WELCOME_SCREEN),
    LOGIN_SCREEN(ScreenConstants.FXML_ROUTE_LOGIN),
    ORDER_LIST_SCREEN(ScreenConstants.FXML_ROUTE_ORDER_LIST_SCREEN),
    ORDER_ADD_SCREEN(ScreenConstants.FXML_ROUTE_ORDER_ADD_SCREEN),
    ORDER_UPDATE_SCREEN(ScreenConstants.FXML_ROUTE_ORDER_UPDATE_SCREEN),
    ORDER_DELETE_SCREEN(ScreenConstants.FXML_ROUTE_ORDER_DELETE_SCREEN);


    private final String route;

    Screens(String route) {
        this.route = route;
    }


}
