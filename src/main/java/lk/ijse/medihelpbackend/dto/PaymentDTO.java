package lk.ijse.medihelpbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private UUID paymentId;
    private UUID appointmentId;
    private double amount;
    private String paymentMethod;
    private Date paymentDate;
    private String status;
}
