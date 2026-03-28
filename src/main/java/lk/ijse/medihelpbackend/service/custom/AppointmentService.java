package lk.ijse.medihelpbackend.service.custom;

import lk.ijse.medihelpbackend.dto.AppointmentDTO;
import java.util.List;

public interface AppointmentService {
    List<AppointmentDTO> getAllAppointments();
    List<AppointmentDTO> getAppointmentsByUser(String email);
    List<AppointmentDTO> getAppointmentsByDoctor(String email);
    AppointmentDTO saveAppointment(AppointmentDTO dto);
    void updateStatus(java.util.UUID id, String status);
}
