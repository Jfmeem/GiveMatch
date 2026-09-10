package com.spl2.givematch.ui;

import com.spl2.givematch.model.User;
import com.spl2.givematch.observer.AuditLogChannel;
import com.spl2.givematch.observer.InAppNotificationChannel;
import com.spl2.givematch.service.*;
import javafx.stage.Stage;

public final class AppContext {

    private static AppContext instance;

    private final AuthService authService = new AuthService();
    private final NeedMatchingService needMatchingService = new NeedMatchingService();
    private final DonationService donationService = new DonationService(needMatchingService);
    private final RequestService requestService = new RequestService();
    private final ReportService reportService = new ReportService();
    private final CategoryService categoryService = new CategoryService();
    private final UserService userService = new UserService();
    private final InAppNotificationChannel inAppChannel = new InAppNotificationChannel();

    private User currentUser;
    private Stage primaryStage;

    private AppContext() {

        needMatchingService.registerObserver(inAppChannel);
        needMatchingService.registerObserver(new AuditLogChannel("givematch-audit.log"));
    }

    public static synchronized AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public NeedMatchingService getNeedMatchingService() {
        return needMatchingService;
    }

    public DonationService getDonationService() {
        return donationService;
    }

    public RequestService getRequestService() {
        return requestService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public UserService getUserService() {
        return userService;
    }

    public InAppNotificationChannel getInAppChannel() {
        return inAppChannel;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
