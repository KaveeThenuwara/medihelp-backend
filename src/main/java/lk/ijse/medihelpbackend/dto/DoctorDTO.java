package lk.ijse.medihelpbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDTO {
    private String email;
    private String specialization;
    private String hospital;
    private String phone;
}
