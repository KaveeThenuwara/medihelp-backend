package lk.ijse.medihelpbackend.service.custom;

import lk.ijse.medihelpbackend.dto.PaymentDTO;
import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAllPayments();
    PaymentDTO savePayment(PaymentDTO dto);
}
