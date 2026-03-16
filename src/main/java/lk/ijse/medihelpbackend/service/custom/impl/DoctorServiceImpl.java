package lk.ijse.medihelpbackend.service.custom.impl;

import lk.ijse.medihelpbackend.Entity.Doctor;
import lk.ijse.medihelpbackend.Entity.User;
import lk.ijse.medihelpbackend.dto.DoctorDTO;
import lk.ijse.medihelpbackend.repo.DoctorRepo;
import lk.ijse.medihelpbackend.repo.UserRepository;
import lk.ijse.medihelpbackend.service.custom.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired private DoctorRepo doctorRepo;
    @Autowired private UserRepository userRepo;
    
    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepo.findAll().stream().map(d -> new DoctorDTO(d.getUser().getEmail(), d.getSpecialization(), d.getHospital(), d.getPhone())).collect(Collectors.toList());
    }
    
    @Override
    public DoctorDTO saveDoctor(DoctorDTO dto) {
        User u = userRepo.findByEmail(dto.getEmail());
        if(u == null) return null;
        Doctor d = doctorRepo.findByUserEmail(dto.getEmail()).orElse(new Doctor());
        d.setUser(u);
        d.setSpecialization(dto.getSpecialization());
        d.setHospital(dto.getHospital());
        d.setPhone(dto.getPhone());
        doctorRepo.save(d);
        return dto;
    }
    
    @Override
    public void deleteDoctor(String email) {
        doctorRepo.findByUserEmail(email).ifPresent(doctorRepo::delete);
    }
}
