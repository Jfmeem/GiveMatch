package com.spl2.givematch.service;

import com.spl2.givematch.dao.DonationDAO;
import com.spl2.givematch.model.Donation;
import com.spl2.givematch.model.DonationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DonationService {

    private final DonationDAO donationDAO = new DonationDAO();
    private final NeedMatchingService needMatchingService;

    public DonationService(NeedMatchingService needMatchingService) {
        this.needMatchingService = needMatchingService;
    }

    public Donation postDonation(int donorId, int categoryId, int quantity, String description) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        Donation donation = new Donation();
        donation.setDonorId(donorId);
        donation.setCategoryId(categoryId);
        donation.setQuantity(quantity);
        donation.setQuantityRemaining(quantity);
        donation.setDescription(description);
        donation.setStatus(DonationStatus.AVAILABLE);
        donation.setCreatedAt(LocalDateTime.now());

        Donation saved = donationDAO.insert(donation);
        needMatchingService.checkForMatches(saved);
        return saved;
    }

    public void closeDonation(int donationId, int donorId) {
        Donation donation = requireOwnedByDonor(donationId, donorId);
        donation.getState().close(donation);
        donationDAO.updateState(donation);
    }

    public List<Donation> getRequestableDonations() {
        return donationDAO.findRequestable();
    }

    public List<Donation> getDonationsByDonor(int donorId) {
        return donationDAO.findByDonor(donorId);
    }

    public List<Donation> getAllDonations() {
        return donationDAO.findAll();
    }

    public Optional<Donation> findById(int donationId) {
        return donationDAO.findById(donationId);
    }

    private Donation requireOwnedByDonor(int donationId, int donorId) {
        Donation donation = donationDAO.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Donation #" + donationId + " does not exist."));
        if (donation.getDonorId() != donorId) {
            throw new IllegalArgumentException("Donation #" + donationId + " does not belong to this donor.");
        }
        return donation;
    }
}
