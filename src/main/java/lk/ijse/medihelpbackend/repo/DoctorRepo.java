package lk.ijse.medihelpbackend.repo;

import lk.ijse.medihelpbackend.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepo extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByUserEmail(String email);
}
