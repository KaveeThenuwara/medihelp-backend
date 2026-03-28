package lk.ijse.medihelpbackend.service.custom;

import lk.ijse.medihelpbackend.dto.DoctorDTO;
import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
    DoctorDTO saveDoctor(DoctorDTO doctorDTO);
    void deleteDoctor(String email);
}
