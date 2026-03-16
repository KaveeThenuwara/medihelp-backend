package lk.ijse.medihelpbackend.service.custom.impl;

import lk.ijse.medihelpbackend.Entity.Appointment;
import lk.ijse.medihelpbackend.Entity.Doctor;
import lk.ijse.medihelpbackend.Entity.User;
import lk.ijse.medihelpbackend.dto.AppointmentDTO;
import lk.ijse.medihelpbackend.repo.AppointmentRepo;
import lk.ijse.medihelpbackend.repo.DoctorRepo;
import lk.ijse.medihelpbackend.repo.UserRepository;
import lk.ijse.medihelpbackend.service.custom.AppointmentService;
import lk.ijse.medihelpbackend.service.custom.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired private AppointmentRepo appointmentRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private DoctorRepo doctorRepo;
    @Autowired private EmailService emailService;
    
    private AppointmentDTO map(Appointment a) {
        return new AppointmentDTO(a.getAppointmentId(), a.getPatient().getEmail(), a.getDoctor().getUser().getEmail(), a.getAppointmentDate(), a.getStatus());
    }
    
    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepo.findAll().stream().map(this::map).collect(Collectors.toList());
    }
    
    @Override
    public List<AppointmentDTO> getAppointmentsByUser(String email) {
        return appointmentRepo.findByPatientEmail(email).stream().map(this::map).collect(Collectors.toList());
    }
    
    @Override
    public List<AppointmentDTO> getAppointmentsByDoctor(String email) {
        return appointmentRepo.findByDoctorUserEmail(email).stream().map(this::map).collect(Collectors.toList());
    }
    
    @Override
    public AppointmentDTO saveAppointment(AppointmentDTO dto) {
        User p = userRepo.findByEmail(dto.getPatientEmail());
        Doctor d = doctorRepo.findByUserEmail(dto.getDoctorEmail()).orElse(null);
        if(p == null || d == null) return null;
        Appointment a = new Appointment();
        a.setPatient(p);
        a.setDoctor(d);
        a.setAppointmentDate(dto.getAppointmentDate());
        a.setStatus("PENDING");
        a = appointmentRepo.save(a);
        
        // Send email notification
        try { emailService.sendAppointmentConfirmation(a); } catch (Exception e) { System.err.println("Email failed: " + e.getMessage()); }
        
        return map(a);
    }
    
    @Override
    public void updateStatus(UUID id, String status) {
        appointmentRepo.findById(id).ifPresent(a -> {
            a.setStatus(status);
            appointmentRepo.save(a);
            // Send update notification
            try { emailService.sendAppointmentConfirmation(a); } catch (Exception e) { System.err.println("Email failed: " + e.getMessage()); }
        });
    }
}
