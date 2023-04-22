package org.example.services;

import com.google.common.hash.Hashing;
import org.models.CharityCase;
import org.models.Donation;
import org.models.Donor;
import org.models.Volunteer;
import org.example.repositories.interfaces.CharityCaseRepository;
import org.example.repositories.interfaces.DonationRepository;
import org.example.repositories.interfaces.DonorRepository;
import org.example.repositories.interfaces.VolunteerRepository;
import org.models.dtos.CharityCaseDto;
import org.services.ITeledonService;
import org.services.Observable;
import org.services.Observer;
import org.services.ServiceException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeledonService implements ITeledonService, Observable {
    private final List<Observer> observers;
    private final CharityCaseRepository charityCaseRepository;
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final VolunteerRepository volunteerRepository;

    public TeledonService(CharityCaseRepository charityCaseRepository, DonationRepository donationRepository, DonorRepository donorRepository, VolunteerRepository volunteerRepository) {
        this.charityCaseRepository = charityCaseRepository;
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.volunteerRepository = volunteerRepository;
        this.observers = new ArrayList<>();
    }

    @Override
    public Iterable<CharityCaseDto> getAllCharityCases() {
        Iterable<CharityCase> charityCases = charityCaseRepository.findAll();
        List<CharityCaseDto> charityCasesDto = new ArrayList<>();
        charityCases.forEach(charityCase -> {
            Double totalAmount = donationRepository.getTotalAmountOfRaisedMoney(charityCase.getId());
            charityCasesDto.add(new CharityCaseDto(charityCase.getId(), charityCase.getCaseName(), charityCase.getDescription(), totalAmount));
        });
        return charityCasesDto;
    }

    @Override
    public void addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount) throws ServiceException {
        System.out.println("Adding new donation for case no: " + charityCaseId);
        if (charityCaseRepository.findOneById(charityCaseId).isEmpty()) {
            throw new ServiceException("No charity case found with id: " + charityCaseId);
        }
        Donor donor = new Donor(name, emailAddress, phoneNumber);
        Donation donation = new Donation(donationAmount, charityCaseId, donor);
        Optional<Donor> donorByPhoneNumber = donorRepository.findDonorByPhoneNumber(phoneNumber);
        if (donorByPhoneNumber.isEmpty()) {
            //the donor does not exist.
            Optional<Donor> optionalDonor = donorRepository.save(donor);
            optionalDonor.ifPresent(value -> donor.setId(value.getId()));
            if (optionalDonor.isEmpty()) {
                throw new ServiceException("Error in adding donor");
            }
        } else {
            Donor foundDonor = donorByPhoneNumber.get();
            if (!foundDonor.getName().equals(name) || !foundDonor.getMailAddress().equals(emailAddress)
                    || !foundDonor.getPhoneNumber().equals(phoneNumber)) {
                throw new ServiceException("Invalid data for the phone number " + phoneNumber);
            }
            donor.setId(foundDonor.getId());
        }
        System.out.println("Service: saving donation");
        donationRepository.save(donation);
        notifyAllObservers();
    }

    @Override
    public Iterable<Donor> getDonorsWithNameContaining(String containsInName) {
        if (containsInName == null || containsInName.equals("")) {
            throw new ServiceException("Invalid string. The value should be not null and not empty");
        }
        String pattern = "%" + containsInName + "%";
        return donorRepository.findDonorByNameLike(pattern);
    }

    @Override
    public boolean authenticateVolunteer(String username, String password) {
        Optional<Volunteer> optionalVolunteer = volunteerRepository.findVolunteerByUsername(username);
        if (optionalVolunteer.isEmpty()) {
            return false;
        }
        String hashedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        Volunteer volunteer = optionalVolunteer.get();
        return volunteer.getPassword().equals(hashedPassword);
    }

    @Override
    public synchronized void addObserver(Observer observer) {
        System.out.println("Adding observer");
        this.observers.add(observer);
    }

    @Override
    public synchronized void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public synchronized void notifyAllObservers() {
        System.out.println("Notifying " + this.observers.size() + " observers");
        this.observers.forEach(Observer::update);
    }

    @Override
    public void logout() {

    }
}
