package com.spl2.givematch.ui;

import com.spl2.givematch.model.Role;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField contactField;
    @FXML
    private ComboBox<Role> roleComboBox;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {

        roleComboBox.getItems().addAll(Role.DONOR, Role.RECEIVER);
    }

    @FXML
    private void onRegister() {
        try {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String contact = contactField.getText().trim();
            Role role = roleComboBox.getValue();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
                errorLabel.setText("Please fill in every field and pick a role.");
                return;
            }

            AppContext.getInstance().getAuthService().register(name, username, password, role, contact);
            SceneManager.switchTo("/com/spl2/givematch/fxml/login.fxml", "GiveMatch — Login");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onBackToLogin() {
        SceneManager.switchTo("/com/spl2/givematch/fxml/login.fxml", "GiveMatch — Login");
    }
}
