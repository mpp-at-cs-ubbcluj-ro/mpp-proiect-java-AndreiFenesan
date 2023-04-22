package org.example.repositories.interfaces;

import org.models.Volunteer;

import java.util.Optional;

public interface VolunteerRepository extends Repository<Long, Volunteer>{
    Optional<Volunteer> findVolunteerByUsername(String username);
}
