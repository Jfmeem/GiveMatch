package com.spl2.givematch.ui;

import com.spl2.givematch.model.Role;
import com.spl2.givematch.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        Optional<User> user = AppContext.getInstance().getAuthService().login(username, password);
        if (user.isEmpty()) {
            errorLabel.setText("Invalid username or password.");
            return;
        }

        AppContext.getInstance().setCurrentUser(user.get());
        Role role = user.get().getRole();
        if (role == Role.DONOR) {
            SceneManager.switchTo("/fxml/donor_dashboard.fxml", "GiveMatch — Donor Dashboard");
        } else if (role == Role.RECEIVER) {
            SceneManager.switchTo("/fxml/receiver_dashboard.fxml", "GiveMatch — Receiver Dashboard");
        } else {
            SceneManager.switchTo("/fxml/admin_dashboard.fxml", "GiveMatch — Admin Dashboard");
        }
    }

    @FXML
    private void onGoToRegister() {
        SceneManager.switchTo("/fxml/register.fxml", "GiveMatch — Create Account");
    }
}
