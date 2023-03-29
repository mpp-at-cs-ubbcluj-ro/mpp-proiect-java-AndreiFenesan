package org.example.services;

import org.example.models.Donation;
import org.example.models.Donor;
import org.example.observer.CharityCaseChangeEvent;
import org.example.observer.Event;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repositories.interfaces.CharityCaseRepository;
import org.example.repositories.interfaces.DonationRepository;
import org.example.repositories.interfaces.DonorRepository;
import org.example.validators.ValidationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonationService implements Observable<Event> {
    private final List<Observer<Event>> observers;
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final CharityCaseRepository charityCaseRepository;

    public DonationService(DonationRepository donationRepository, DonorRepository donorRepository, CharityCaseRepository charityCaseRepository) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.charityCaseRepository = charityCaseRepository;
        this.observers = new ArrayList<>();
    }

    public boolean addNewDonation(Long charityCaseId, String name, String emailAddress, String phoneNumber, Double donationAmount) throws ServiceException, ValidationError {
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
                return false;
            }
        } else {
            Donor foundDonor = donorByPhoneNumber.get();
            if (!foundDonor.getName().equals(name) || !foundDonor.getMailAddress().equals(emailAddress)
                    || !foundDonor.getPhoneNumber().equals(phoneNumber)) {
                throw new ServiceException("Invalid data for the phone number " + phoneNumber);
            }
            donor.setId(foundDonor.getId());
        }
        if (donationRepository.save(donation).isPresent()) {
            this.notifyAllObservers(new CharityCaseChangeEvent());
            return true;
        }
        return false;
    }

    @Override
    public void addObserver(Observer<Event> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Event> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(Event event) {
        this.observers.forEach(observer -> observer.update(event));
    }
}
