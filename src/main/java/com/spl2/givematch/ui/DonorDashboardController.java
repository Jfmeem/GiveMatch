package com.spl2.givematch.ui;

import com.spl2.givematch.model.Category;
import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DonorDashboardController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Label postErrorLabel;

    @FXML
    private TableView<Donation> donationsTable;
    @FXML
    private TableColumn<Donation, String> colDonationDescription;
    @FXML
    private TableColumn<Donation, String> colDonationQuantity;
    @FXML
    private TableColumn<Donation, String> colDonationStatus;

    @FXML
    private TableView<Request> requestsTable;
    @FXML
    private TableColumn<Request, String> colRequestReceiver;
    @FXML
    private TableColumn<Request, String> colRequestQuantity;
    @FXML
    private TableColumn<Request, String> colRequestAllocated;
    @FXML
    private TableColumn<Request, String> colRequestStatus;

    private User currentUser;

    @FXML
    private void initialize() {
        currentUser = AppContext.getInstance().getCurrentUser();
        welcomeLabel.setText("Donor: " + currentUser.getName());

        categoryComboBox.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getCategoryService().getAllCategories()));

        colDonationDescription.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getDescription()));
        colDonationQuantity.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getQuantityRemaining() + " / " + row.getValue().getQuantity()));
        colDonationStatus.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getStatus().name()));

        colRequestReceiver.setCellValueFactory(row -> new SimpleStringProperty(
                AppContext.getInstance().getUserService().findById(row.getValue().getReceiverId())
                        .map(User::getName).orElse("Unknown")));
        colRequestQuantity.setCellValueFactory(row ->
                new SimpleStringProperty(String.valueOf(row.getValue().getQuantityRequested())));
        colRequestAllocated.setCellValueFactory(row ->
                new SimpleStringProperty(String.valueOf(row.getValue().getQuantityAllocated())));
        colRequestStatus.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getStatus().name()));

        donationsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadRequestsForSelectedDonation());

        reloadDonations();
    }

    @FXML
    private void onPostDonation() {
        postErrorLabel.setText("");
        try {
            Category category = categoryComboBox.getValue();
            if (category == null) {
                postErrorLabel.setText("Pick a category first.");
                return;
            }
            int quantity = Integer.parseInt(quantityField.getText().trim());
            String description = descriptionField.getText().trim();

            AppContext.getInstance().getDonationService()
                    .postDonation(currentUser.getId(), category.getId(), quantity, description);

            quantityField.clear();
            descriptionField.clear();
            reloadDonations();
        } catch (NumberFormatException e) {
            postErrorLabel.setText("Quantity must be a whole number.");
        } catch (IllegalArgumentException e) {
            postErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onCloseDonation() {
        Donation selected = donationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Select a donation first.");
            return;
        }
        try {
            AppContext.getInstance().getDonationService().closeDonation(selected.getId(), currentUser.getId());
            reloadDonations();
        } catch (RuntimeException e) {
            showInfo(e.getMessage());
        }
    }

    @FXML
    private void onProcessRequests() {
        Donation selected = donationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Select a donation first.");
            return;
        }
        AppContext.getInstance().getRequestService().processRequestsForDonation(selected.getId());
        reloadDonations();
        loadRequestsForSelectedDonation();
    }

    @FXML
    private void onCompleteRequest() {
        Request selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Select a request first.");
            return;
        }
        try {
            AppContext.getInstance().getRequestService().completeRequest(selected.getId());
            loadRequestsForSelectedDonation();
        } catch (RuntimeException e) {
            showInfo(e.getMessage());
        }
    }

    @FXML
    private void onRefresh() {
        reloadDonations();
        loadRequestsForSelectedDonation();
    }

    @FXML
    private void onLogout() {
        AppContext.getInstance().setCurrentUser(null);
        SceneManager.switchTo("/com/spl2/givematch/fxml/login.fxml", "GiveMatch — Login");
    }

    private void reloadDonations() {
        donationsTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getDonationService().getDonationsByDonor(currentUser.getId())));
    }

    private void loadRequestsForSelectedDonation() {
        Donation selected = donationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            requestsTable.setItems(FXCollections.observableArrayList());
            return;
        }
        requestsTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getRequestService().getRequestsForDonation(selected.getId())));
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
}
