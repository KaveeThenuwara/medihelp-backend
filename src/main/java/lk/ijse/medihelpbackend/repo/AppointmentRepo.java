package lk.ijse.medihelpbackend.repo;

import lk.ijse.medihelpbackend.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepo extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByPatientEmail(String email);
    List<Appointment> findByDoctorUserEmail(String email);
}
