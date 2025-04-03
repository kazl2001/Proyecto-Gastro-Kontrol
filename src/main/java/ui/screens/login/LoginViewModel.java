package ui.screens.login;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import services.LoginService;
import ui.screens.common.BaseViewModel;

public final class LoginViewModel extends BaseViewModel {

    private final LoginService ls;

    @Inject
    public LoginViewModel(LoginService ls) {
        super(new LoginState(null, false,0));
        this.ls = ls;
        this._state = new SimpleObjectProperty<>(new LoginState(null, false,0));
    }

    private final ObjectProperty<LoginState> _state;

    public ReadOnlyObjectProperty<LoginState> getState() {
        return _state;
    }

    public void login(String user, String pass) {
        ls.login(user, pass)
                .peek(id -> _state.setValue(new LoginState(null, false, id)))
                .peekLeft(error -> _state.setValue(new LoginState(error, false, 0)));
    }
    public void checkAdmin(String user, String pass) {
        if (ls.checkAdmin(user, pass)) {
            _state.setValue(new LoginState(null, ls.checkAdmin(user, pass),0));
        }
    }
}
