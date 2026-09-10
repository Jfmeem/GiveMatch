package com.spl2.givematch.service;

import com.spl2.givematch.dao.*;
import com.spl2.givematch.model.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReportService {

    private final NeedRequestDAO needRequestDAO = new NeedRequestDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final DonationDAO donationDAO = new DonationDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final UserDAO userDAO = new UserDAO();

    public List<CategoryDemand> unmetNeedsByCategory() {
        Map<Integer, Category> categories = indexCategories();
        Map<Integer, int[]> countAndQuantityByCategory = new HashMap<>(); // [count, totalQuantity]

        for (NeedRequest need : needRequestDAO.findAllOpen()) {
            int[] bucket = countAndQuantityByCategory.computeIfAbsent(need.getCategoryId(), id -> new int[2]);
            bucket[0]++;
            bucket[1] += need.getQuantityNeeded();
        }

        List<CategoryDemand> rows = new ArrayList<>();
        for (Map.Entry<Integer, int[]> entry : countAndQuantityByCategory.entrySet()) {
            String name = categories.containsKey(entry.getKey()) ? categories.get(entry.getKey()).getName() : "Unknown";
            rows.add(new CategoryDemand(name, entry.getValue()[0], entry.getValue()[1]));
        }
        rows.sort(Comparator.comparingInt(CategoryDemand::getTotalQuantityNeeded).reversed());
        return rows;
    }

    public List<FulfillmentStat> fulfillmentRateByDonor() {
        Map<Integer, Donation> donationsById = indexDonations();
        Map<Integer, int[]> requestedAndDistributedByDonor = new HashMap<>();

        for (Request request : requestDAO.findAll()) {
            Donation donation = donationsById.get(request.getDonationId());
            if (donation == null) continue;
            int[] bucket = requestedAndDistributedByDonor.computeIfAbsent(donation.getDonorId(), id -> new int[2]);
            bucket[0] += request.getQuantityRequested();
            if (request.getStatus() == RequestStatus.COMPLETED) {
                bucket[1] += request.getQuantityAllocated();
            }
        }

        List<FulfillmentStat> rows = new ArrayList<>();
        for (Map.Entry<Integer, int[]> entry : requestedAndDistributedByDonor.entrySet()) {
            String name = userDAO.findById(entry.getKey()).map(User::getName).orElse("Unknown donor");
            rows.add(new FulfillmentStat(name, entry.getValue()[0], entry.getValue()[1]));
        }
        rows.sort(Comparator.comparingDouble(FulfillmentStat::getFulfillmentRatePercent).reversed());
        return rows;
    }

    public List<FulfillmentStat> fulfillmentRateByCategory() {
        Map<Integer, Donation> donationsById = indexDonations();
        Map<Integer, Category> categories = indexCategories();
        Map<Integer, int[]> requestedAndDistributedByCategory = new HashMap<>();

        for (Request request : requestDAO.findAll()) {
            Donation donation = donationsById.get(request.getDonationId());
            if (donation == null) continue;
            int[] bucket = requestedAndDistributedByCategory.computeIfAbsent(donation.getCategoryId(), id -> new int[2]);
            bucket[0] += request.getQuantityRequested();
            if (request.getStatus() == RequestStatus.COMPLETED) {
                bucket[1] += request.getQuantityAllocated();
            }
        }

        List<FulfillmentStat> rows = new ArrayList<>();
        for (Map.Entry<Integer, int[]> entry : requestedAndDistributedByCategory.entrySet()) {
            String name = categories.containsKey(entry.getKey()) ? categories.get(entry.getKey()).getName() : "Unknown";
            rows.add(new FulfillmentStat(name, entry.getValue()[0], entry.getValue()[1]));
        }
        rows.sort(Comparator.comparingDouble(FulfillmentStat::getFulfillmentRatePercent).reversed());
        return rows;
    }

    public List<FulfillmentStat> fulfillmentRateByMonth() {
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, int[]> requestedAndDistributedByMonth = new HashMap<>();

        for (Request request : requestDAO.findAll()) {
            String month = request.getCreatedAt().format(monthFormat);
            int[] bucket = requestedAndDistributedByMonth.computeIfAbsent(month, m -> new int[2]);
            bucket[0] += request.getQuantityRequested();
            if (request.getStatus() == RequestStatus.COMPLETED) {
                bucket[1] += request.getQuantityAllocated();
            }
        }

        List<FulfillmentStat> rows = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : requestedAndDistributedByMonth.entrySet()) {
            rows.add(new FulfillmentStat(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
        }
        rows.sort(Comparator.comparing(FulfillmentStat::getLabel));
        return rows;
    }

    private Map<Integer, Category> indexCategories() {
        Map<Integer, Category> map = new HashMap<>();
        for (Category category : categoryDAO.findAll()) {
            map.put(category.getId(), category);
        }
        return map;
    }

    private Map<Integer, Donation> indexDonations() {
        Map<Integer, Donation> map = new HashMap<>();
        for (Donation donation : donationDAO.findAll()) {
            map.put(donation.getId(), donation);
        }
        return map;
    }
}
