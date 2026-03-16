package lk.ijse.medihelpbackend.service.custom.impl;

import lk.ijse.medihelpbackend.Entity.Appointment;
import lk.ijse.medihelpbackend.Entity.Payment;
import lk.ijse.medihelpbackend.dto.PaymentDTO;
import lk.ijse.medihelpbackend.repo.AppointmentRepo;
import lk.ijse.medihelpbackend.repo.PaymentRepo;
import lk.ijse.medihelpbackend.service.custom.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired private PaymentRepo paymentRepo;
    @Autowired private AppointmentRepo appointmentRepo;
    
    private PaymentDTO map(Payment p) {
        return new PaymentDTO(p.getPaymentId(), p.getAppointment().getAppointmentId(), p.getAmount(), p.getPaymentMethod(), p.getPaymentDate(), p.getStatus());
    }
    
    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepo.findAll().stream().map(this::map).collect(Collectors.toList());
    }
    
    @Override
    public PaymentDTO savePayment(PaymentDTO dto) {
        Appointment a = appointmentRepo.findById(dto.getAppointmentId()).orElse(null);
        if(a == null) return null;
        Payment p = new Payment();
        p.setAppointment(a);
        p.setAmount(dto.getAmount());
        p.setPaymentMethod(dto.getPaymentMethod());
        p.setPaymentDate(new java.sql.Date(System.currentTimeMillis()));
        p.setStatus("COMPLETED");
        p = paymentRepo.save(p);
        a.setStatus("PAID");
        appointmentRepo.save(a);
        return map(p);
    }
}
