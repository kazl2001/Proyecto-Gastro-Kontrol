package ui.screens.login;

import lombok.Getter;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

@Getter
public final class LoginState extends BaseState {

    final boolean isAdmin;
    final int customerId;

    public LoginState(RestaurantError error, boolean isAdmin, int customerId) {
        super(error);
        this.isAdmin = isAdmin;
        this.customerId = customerId;
    }
}
