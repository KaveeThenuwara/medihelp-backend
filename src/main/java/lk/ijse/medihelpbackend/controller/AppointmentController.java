package lk.ijse.medihelpbackend.controller;

import lk.ijse.medihelpbackend.dto.AppointmentDTO;
import lk.ijse.medihelpbackend.dto.ResponseDTO;
import lk.ijse.medihelpbackend.service.custom.AppointmentService;
import lk.ijse.medihelpbackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/appointments")
@CrossOrigin
public class AppointmentController {

    @Autowired 
    private AppointmentService appointmentService;
    
    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() { 
        try {
            List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Success", appointments), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/{email}")
    public ResponseEntity<ResponseDTO> getByUser(@PathVariable String email) { 
        try {
            List<AppointmentDTO> appointments = appointmentService.getAppointmentsByUser(email);
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Success", appointments), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/doctor/{email}")
    public ResponseEntity<ResponseDTO> getByDoctor(@PathVariable String email) { 
        try {
            List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDoctor(email);
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Success", appointments), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<ResponseDTO> save(@RequestBody AppointmentDTO dto) { 
        try {
            AppointmentDTO savedAppointment = appointmentService.saveAppointment(dto);
            if (savedAppointment != null) {
                return new ResponseEntity<>(new ResponseDTO(VarList.Created, "Appointment created successfully", savedAppointment), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseDTO(VarList.Not_Found, "Patient or Doctor not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseDTO> updateStatus(@PathVariable UUID id, @RequestParam String status) { 
        try {
            appointmentService.updateStatus(id, status);
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Appointment status updated", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
