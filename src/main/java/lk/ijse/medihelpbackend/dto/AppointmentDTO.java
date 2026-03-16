package lk.ijse.medihelpbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {
    private UUID appointmentId;
    private String patientEmail;
    private String doctorEmail;
    private Date appointmentDate;
    private String status;
}
