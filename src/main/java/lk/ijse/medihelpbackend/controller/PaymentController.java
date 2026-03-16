package lk.ijse.medihelpbackend.controller;

import lk.ijse.medihelpbackend.dto.PaymentDTO;
import lk.ijse.medihelpbackend.dto.ResponseDTO;
import lk.ijse.medihelpbackend.service.custom.PaymentService;
import lk.ijse.medihelpbackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
@CrossOrigin
public class PaymentController {

    @Autowired 
    private PaymentService paymentService;
    
    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() { 
        try {
            List<PaymentDTO> payments = paymentService.getAllPayments();
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Success", payments), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<ResponseDTO> save(@RequestBody PaymentDTO dto) { 
        try {
            PaymentDTO savedPayment = paymentService.savePayment(dto);
            if (savedPayment != null) {
                return new ResponseEntity<>(new ResponseDTO(VarList.Created, "Payment created successfully", savedPayment), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseDTO(VarList.Not_Found, "Appointment not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
