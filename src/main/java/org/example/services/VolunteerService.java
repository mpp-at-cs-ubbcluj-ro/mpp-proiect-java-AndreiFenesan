package org.example.services;

import com.google.common.hash.Hashing;
import org.example.models.Volunteer;
import org.example.repositories.interfaces.VolunteerRepository;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public boolean authenticateVolunteer(String username, String password) {
        Optional<Volunteer> optionalVolunteer = volunteerRepository.findVolunteerByUsername(username);
        if (optionalVolunteer.isEmpty()) {
            return false;
        }
        String hashedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        Volunteer volunteer = optionalVolunteer.get();
        return volunteer.getPassword().equals(hashedPassword);
    }
}
