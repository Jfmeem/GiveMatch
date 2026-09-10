package com.spl2.givematch.ui;

import com.spl2.givematch.model.*;
import com.spl2.givematch.observer.NotificationMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiverDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ComboBox<Category> categoryFilterComboBox;
    @FXML
    private TableView<Donation> donationsTable;
    @FXML
    private TableColumn<Donation, String> colBrowseDescription;
    @FXML
    private TableColumn<Donation, String> colBrowseCategory;
    @FXML
    private TableColumn<Donation, String> colBrowseRemaining;
    @FXML
    private TextField requestQuantityField;
    @FXML
    private Label requestErrorLabel;

    @FXML
    private TableView<Request> myRequestsTable;
    @FXML
    private TableColumn<Request, String> colMyRequestDonation;
    @FXML
    private TableColumn<Request, String> colMyRequestQuantity;
    @FXML
    private TableColumn<Request, String> colMyRequestAllocated;
    @FXML
    private TableColumn<Request, String> colMyRequestStatus;

    @FXML
    private ComboBox<Category> needCategoryComboBox;
    @FXML
    private TextField needQuantityField;
    @FXML
    private Label needErrorLabel;
    @FXML
    private TableView<NeedRequest> myNeedsTable;
    @FXML
    private TableColumn<NeedRequest, String> colNeedCategory;
    @FXML
    private TableColumn<NeedRequest, String> colNeedQuantity;
    @FXML
    private TableColumn<NeedRequest, String> colNeedStatus;

    @FXML
    private ListView<String> notificationsListView;

    private User currentUser;

    @FXML
    private void initialize() {
        currentUser = AppContext.getInstance().getCurrentUser();
        welcomeLabel.setText("Receiver: " + currentUser.getName());

        List<Category> categories = AppContext.getInstance().getCategoryService().getAllCategories();
        categoryFilterComboBox.setItems(FXCollections.observableArrayList(categories));
        needCategoryComboBox.setItems(FXCollections.observableArrayList(categories));

        colBrowseDescription.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getDescription()));
        colBrowseCategory.setCellValueFactory(row -> new SimpleStringProperty(categoryName(row.getValue().getCategoryId())));
        colBrowseRemaining.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getQuantityRemaining())));

        colMyRequestDonation.setCellValueFactory(row -> new SimpleStringProperty(
                AppContext.getInstance().getDonationService().findById(row.getValue().getDonationId())
                        .map(Donation::getDescription).orElse("(deleted)")));
        colMyRequestQuantity.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getQuantityRequested())));
        colMyRequestAllocated.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getQuantityAllocated())));
        colMyRequestStatus.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getStatus().name()));

        colNeedCategory.setCellValueFactory(row -> new SimpleStringProperty(categoryName(row.getValue().getCategoryId())));
        colNeedQuantity.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getQuantityNeeded())));
        colNeedStatus.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getStatus().name()));

        onRefreshDonations();
        onRefreshMyRequests();
        onRefreshMyNeeds();
        onRefreshNotifications();
    }

    @FXML
    private void onRefreshDonations() {
        List<Donation> all = AppContext.getInstance().getDonationService().getRequestableDonations();
        Category filter = categoryFilterComboBox.getValue();
        if (filter != null) {
            all = all.stream().filter(d -> d.getCategoryId() == filter.getId()).toList();
        }
        donationsTable.setItems(FXCollections.observableArrayList(all));
    }

    @FXML
    private void onCreateRequest() {
        requestErrorLabel.setText("");
        Donation selected = donationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            requestErrorLabel.setText("Select a donation first.");
            return;
        }
        try {
            int quantity = Integer.parseInt(requestQuantityField.getText().trim());
            AppContext.getInstance().getRequestService().createRequest(currentUser.getId(), selected.getId(), quantity);
            requestQuantityField.clear();
            onRefreshDonations();
            onRefreshMyRequests();
        } catch (NumberFormatException e) {
            requestErrorLabel.setText("Quantity must be a whole number.");
        } catch (RuntimeException e) {
            requestErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onRefreshMyRequests() {
        myRequestsTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getRequestService().getRequestsByReceiver(currentUser.getId())));
    }

    @FXML
    private void onRegisterNeed() {
        needErrorLabel.setText("");
        Category category = needCategoryComboBox.getValue();
        if (category == null) {
            needErrorLabel.setText("Pick a category first.");
            return;
        }
        try {
            int quantity = Integer.parseInt(needQuantityField.getText().trim());
            AppContext.getInstance().getNeedMatchingService().registerNeed(currentUser.getId(), category.getId(), quantity);
            needQuantityField.clear();
            onRefreshMyNeeds();
        } catch (NumberFormatException e) {
            needErrorLabel.setText("Quantity must be a whole number.");
        } catch (RuntimeException e) {
            needErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onRefreshMyNeeds() {
        myNeedsTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getNeedMatchingService().getNeedsForReceiver(currentUser.getId())));
    }

    @FXML
    private void onRefreshNotifications() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d, HH:mm");
        List<NotificationMessage> inbox = AppContext.getInstance().getInAppChannel().getInbox(currentUser.getId());
        List<String> lines = inbox.stream()
                .map(m -> "[" + m.getOccurredAt().format(format) + "] " + m.getText())
                .toList();
        notificationsListView.setItems(FXCollections.observableArrayList(lines));
    }

    @FXML
    private void onLogout() {
        AppContext.getInstance().setCurrentUser(null);
        SceneManager.switchTo("/fxml/login.fxml", "GiveMatch — Login");
    }

    private String categoryName(int categoryId) {
        return AppContext.getInstance().getCategoryService().getAllCategories().stream()
                .filter(c -> c.getId() == categoryId)
                .map(Category::getName)
                .findFirst()
                .orElse("Unknown");
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }
}
