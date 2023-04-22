package org.example.repositories.interfaces;

import org.models.Donation;

public interface DonationRepository extends Repository<Long, Donation>{
    double getTotalAmountOfRaisedMoney(Long charityCaseId);
}
