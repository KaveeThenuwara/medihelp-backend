package lk.ijse.medihelpbackend.repo;

import lk.ijse.medihelpbackend.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    //payment
}
