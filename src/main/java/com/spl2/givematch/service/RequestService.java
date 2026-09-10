package com.spl2.givematch.service;

import com.spl2.givematch.dao.DistributionDAO;
import com.spl2.givematch.dao.DonationDAO;
import com.spl2.givematch.dao.RequestDAO;
import com.spl2.givematch.dao.UserDAO;
import com.spl2.givematch.model.Distribution;
import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.Request;
import com.spl2.givematch.model.RequestStatus;
import com.spl2.givematch.strategy.AllocationResult;
import com.spl2.givematch.strategy.AllocationStrategy;
import com.spl2.givematch.strategy.AllocationStrategyFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestService {

    private final RequestDAO requestDAO = new RequestDAO();
    private final DonationDAO donationDAO = new DonationDAO();
    private final DistributionDAO distributionDAO = new DistributionDAO();
    private final UserDAO userDAO = new UserDAO();

    private AllocationStrategy allocationStrategy = AllocationStrategyFactory.create(AllocationStrategyFactory.Type.FIFO);

    public void setAllocationStrategy(AllocationStrategyFactory.Type type) {
        this.allocationStrategy = AllocationStrategyFactory.create(type);
    }

    public String getCurrentStrategyName() {
        return allocationStrategy.getDisplayName();
    }

    public Request createRequest(int receiverId, int donationId, int quantityRequested) {
        Donation donation = donationDAO.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Donation #" + donationId + " does not exist."));
        if (!donation.getState().canBeRequested()) {
            throw new IllegalStateException("Donation #" + donationId + " is not accepting requests right now.");
        }
        if (quantityRequested <= 0) {
            throw new IllegalArgumentException("Requested quantity must be positive.");
        }

        Request request = new Request();
        request.setDonationId(donationId);
        request.setReceiverId(receiverId);
        request.setQuantityRequested(quantityRequested);
        request.setQuantityAllocated(0);
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        Request saved = requestDAO.insert(request);

        processRequestsForDonation(donationId);
        return saved;
    }

    /**
     * Runs every PENDING request against a donation through the currently configured
     * AllocationStrategy and applies the results. Safe to call any time (e.g. every time a
     * new request comes in, or from an Admin/Donor "process requests" button) — if there's
     * nothing pending, or nothing left to give, it simply does nothing.
     */
    public void processRequestsForDonation(int donationId) {
        Donation donation = donationDAO.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Donation #" + donationId + " does not exist."));
        List<Request> pending = requestDAO.findPendingByDonation(donationId);
        if (pending.isEmpty() || donation.getQuantityRemaining() <= 0) {
            return;
        }

        Map<Integer, Integer> priorityByReceiver = new HashMap<>();
        for (Request request : pending) {
            userDAO.findById(request.getReceiverId())
                    .ifPresent(user -> priorityByReceiver.put(user.getId(), user.getPriorityLevel()));
        }

        List<AllocationResult> results = allocationStrategy.allocate(
                donation.getQuantityRemaining(), pending, priorityByReceiver);

        for (AllocationResult result : results) {
            Request request = result.getRequest();
            if (result.isGranted()) {
                request.getState().accept(request, result.getQuantityGranted());
                requestDAO.updateState(request);
                donation.getState().claim(donation, result.getQuantityGranted());
            } else {
                request.getState().reject(request);
                requestDAO.updateState(request);
            }
        }
        donationDAO.updateState(donation);
    }

    public Distribution completeRequest(int requestId) {
        Request request = requestDAO.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request #" + requestId + " does not exist."));

        request.getState().complete(request);
        requestDAO.updateState(request);

        Distribution distribution = new Distribution();
        distribution.setRequestId(request.getId());
        distribution.setQuantityDistributed(request.getQuantityAllocated());
        distribution.setDistributedAt(LocalDateTime.now());
        return distributionDAO.insert(distribution);
    }

    public List<Request> getRequestsForDonation(int donationId) {
        return requestDAO.findByDonation(donationId);
    }

    public List<Request> getRequestsByReceiver(int receiverId) {
        return requestDAO.findByReceiver(receiverId);
    }

    public List<Request> getAllRequests() {
        return requestDAO.findAll();
    }
}
