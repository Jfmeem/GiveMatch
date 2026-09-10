package com.spl2.givematch.service;

import com.spl2.givematch.dao.CategoryDAO;
import com.spl2.givematch.dao.NeedRequestDAO;
import com.spl2.givematch.model.Category;
import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.NeedRequest;
import com.spl2.givematch.model.NeedRequestStatus;
import com.spl2.givematch.observer.DonationNotificationCenter;
import com.spl2.givematch.observer.DonationObserver;
import com.spl2.givematch.observer.NotificationMessage;

import java.time.LocalDateTime;
import java.util.List;

public class NeedMatchingService {

    private final NeedRequestDAO needRequestDAO = new NeedRequestDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final DonationNotificationCenter notificationCenter = new DonationNotificationCenter();

    public void registerObserver(DonationObserver observer) {
        notificationCenter.addObserver(observer);
    }

    public NeedRequest registerNeed(int receiverId, int categoryId, int quantityNeeded) {
        NeedRequest need = new NeedRequest();
        need.setReceiverId(receiverId);
        need.setCategoryId(categoryId);
        need.setQuantityNeeded(quantityNeeded);
        need.setStatus(NeedRequestStatus.OPEN);
        need.setCreatedAt(LocalDateTime.now());
        return needRequestDAO.insert(need);
    }

    public void checkForMatches(Donation donation) {
        List<NeedRequest> openNeeds = needRequestDAO.findOpenByCategory(donation.getCategoryId());
        if (openNeeds.isEmpty()) {
            return;
        }
        String categoryName = categoryDAO.findById(donation.getCategoryId())
                .map(Category::getName).orElse("a category");

        for (NeedRequest need : openNeeds) {
            String text = "A new donation (\"" + donation.getDescription() + "\", x"
                    + donation.getQuantityRemaining() + ") matches your standing need for " + categoryName + ".";
            notificationCenter.notifyObservers(new NotificationMessage(
                    need.getReceiverId(), donation.getId(), donation.getCategoryId(), text));
        }
    }

    public void markFulfilled(int needId) {
        needRequestDAO.updateStatus(needId, NeedRequestStatus.FULFILLED);
    }

    public List<NeedRequest> getNeedsForReceiver(int receiverId) {
        return needRequestDAO.findByReceiver(receiverId);
    }

    public List<NeedRequest> getAllOpenNeeds() {
        return needRequestDAO.findAllOpen();
    }
}
