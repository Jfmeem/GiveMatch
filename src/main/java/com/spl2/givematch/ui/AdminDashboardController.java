package com.spl2.givematch.ui;

import com.spl2.givematch.model.Category;
import com.spl2.givematch.model.User;
import com.spl2.givematch.service.CategoryDemand;
import com.spl2.givematch.service.FulfillmentStat;
import com.spl2.givematch.strategy.AllocationStrategyFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AdminDashboardController {

    @FXML
    private TextField categoryNameField;
    @FXML
    private TextField categoryDescriptionField;
    @FXML
    private Label categoryErrorLabel;
    @FXML
    private TableView<Category> categoriesTable;
    @FXML
    private TableColumn<Category, String> colCategoryName;
    @FXML
    private TableColumn<Category, String> colCategoryDescription;

    @FXML
    private ComboBox<String> strategyComboBox;
    @FXML
    private Label currentStrategyLabel;

    @FXML
    private TextField usernameLookupField;
    @FXML
    private Label foundUserLabel;
    @FXML
    private TextField priorityLevelField;
    @FXML
    private Label priorityErrorLabel;

    @FXML
    private TableView<CategoryDemand> unmetNeedsTable;
    @FXML
    private TableColumn<CategoryDemand, String> colUnmetCategory;
    @FXML
    private TableColumn<CategoryDemand, String> colUnmetCount;
    @FXML
    private TableColumn<CategoryDemand, String> colUnmetQuantity;

    @FXML
    private TableView<FulfillmentStat> fulfillmentByDonorTable;
    @FXML
    private TableColumn<FulfillmentStat, String> colDonorLabel;
    @FXML
    private TableColumn<FulfillmentStat, String> colDonorRequested;
    @FXML
    private TableColumn<FulfillmentStat, String> colDonorDistributed;
    @FXML
    private TableColumn<FulfillmentStat, String> colDonorRate;

    @FXML
    private TableView<FulfillmentStat> fulfillmentByCategoryTable;
    @FXML
    private TableColumn<FulfillmentStat, String> colCatLabel;
    @FXML
    private TableColumn<FulfillmentStat, String> colCatRequested;
    @FXML
    private TableColumn<FulfillmentStat, String> colCatDistributed;
    @FXML
    private TableColumn<FulfillmentStat, String> colCatRate;

    @FXML
    private TableView<FulfillmentStat> fulfillmentByMonthTable;
    @FXML
    private TableColumn<FulfillmentStat, String> colMonthLabel;
    @FXML
    private TableColumn<FulfillmentStat, String> colMonthRequested;
    @FXML
    private TableColumn<FulfillmentStat, String> colMonthDistributed;
    @FXML
    private TableColumn<FulfillmentStat, String> colMonthRate;

    private final Map<String, AllocationStrategyFactory.Type> strategyByDisplayName = new LinkedHashMap<>();
    private User foundUser;

    @FXML
    private void initialize() {
        colCategoryName.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getName()));
        colCategoryDescription.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getDescription()));

        colUnmetCategory.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getCategoryName()));
        colUnmetCount.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getOpenNeedCount())));
        colUnmetQuantity.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getTotalQuantityNeeded())));

        wireFulfillmentColumns(colDonorLabel, colDonorRequested, colDonorDistributed, colDonorRate);
        wireFulfillmentColumns(colCatLabel, colCatRequested, colCatDistributed, colCatRate);
        wireFulfillmentColumns(colMonthLabel, colMonthRequested, colMonthDistributed, colMonthRate);

        for (AllocationStrategyFactory.Type type : AllocationStrategyFactory.Type.values()) {
            strategyByDisplayName.put(AllocationStrategyFactory.create(type).getDisplayName(), type);
        }
        strategyComboBox.setItems(FXCollections.observableArrayList(strategyByDisplayName.keySet()));
        currentStrategyLabel.setText("Current strategy: " + AppContext.getInstance().getRequestService().getCurrentStrategyName());

        reloadCategories();
        onRefreshUnmetNeeds();
        onRefreshFulfillmentByDonor();
        onRefreshFulfillmentByCategory();
        onRefreshFulfillmentByMonth();
    }

    private void wireFulfillmentColumns(TableColumn<FulfillmentStat, String> label,
                                        TableColumn<FulfillmentStat, String> requested,
                                        TableColumn<FulfillmentStat, String> distributed,
                                        TableColumn<FulfillmentStat, String> rate) {
        label.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().getLabel()));
        requested.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getTotalRequested())));
        distributed.setCellValueFactory(row -> new SimpleStringProperty(String.valueOf(row.getValue().getTotalDistributed())));
        rate.setCellValueFactory(row -> new SimpleStringProperty(String.format("%.1f", row.getValue().getFulfillmentRatePercent())));
    }

    @FXML
    private void onAddCategory() {
        categoryErrorLabel.setText("");
        try {
            AppContext.getInstance().getCategoryService()
                    .createCategory(categoryNameField.getText().trim(), categoryDescriptionField.getText().trim());
            categoryNameField.clear();
            categoryDescriptionField.clear();
            reloadCategories();
        } catch (IllegalArgumentException e) {
            categoryErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onApplyStrategy() {
        String selected = strategyComboBox.getValue();
        if (selected == null) {
            return;
        }
        AppContext.getInstance().getRequestService().setAllocationStrategy(strategyByDisplayName.get(selected));
        currentStrategyLabel.setText("Current strategy: " + AppContext.getInstance().getRequestService().getCurrentStrategyName());
    }

    @FXML
    private void onFindUser() {
        priorityErrorLabel.setText("");
        Optional<User> user = AppContext.getInstance().getUserService().findByUsername(usernameLookupField.getText().trim());
        if (user.isEmpty()) {
            foundUserLabel.setText("No user found with that username.");
            foundUser = null;
        } else {
            foundUser = user.get();
            foundUserLabel.setText(foundUser.getName() + " (" + foundUser.getRole() + ") — current priority: "
                    + foundUser.getPriorityLevel());
        }
    }

    @FXML
    private void onSetPriority() {
        priorityErrorLabel.setText("");
        if (foundUser == null) {
            priorityErrorLabel.setText("Find a user first.");
            return;
        }
        try {
            int priority = Integer.parseInt(priorityLevelField.getText().trim());
            AppContext.getInstance().getUserService().setPriorityLevel(foundUser.getId(), priority);
            onFindUser();
            priorityLevelField.clear();
        } catch (NumberFormatException e) {
            priorityErrorLabel.setText("Priority level must be a whole number.");
        }
    }

    @FXML
    private void onRefreshUnmetNeeds() {
        unmetNeedsTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getReportService().unmetNeedsByCategory()));
    }

    @FXML
    private void onRefreshFulfillmentByDonor() {
        fulfillmentByDonorTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getReportService().fulfillmentRateByDonor()));
    }

    @FXML
    private void onRefreshFulfillmentByCategory() {
        fulfillmentByCategoryTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getReportService().fulfillmentRateByCategory()));
    }

    @FXML
    private void onRefreshFulfillmentByMonth() {
        fulfillmentByMonthTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getReportService().fulfillmentRateByMonth()));
    }

    @FXML
    private void onLogout() {
        AppContext.getInstance().setCurrentUser(null);
        SceneManager.switchTo("/com/spl2/givematch/fxml/login.fxml", "GiveMatch — Login");
    }

    private void reloadCategories() {
        categoriesTable.setItems(FXCollections.observableArrayList(
                AppContext.getInstance().getCategoryService().getAllCategories()));
    }
}
