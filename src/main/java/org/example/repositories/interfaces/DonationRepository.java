package org.example.repositories.interfaces;

import org.example.models.Donation;

public interface DonationRepository extends Repository<Long, Donation>{
    double getTotalAmountOfRaisedMoney(Long charityCaseId);
}
