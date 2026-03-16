package lk.ijse.medihelpbackend.controller;

import lk.ijse.medihelpbackend.dto.ResponseDTO;
import lk.ijse.medihelpbackend.service.custom.AppointmentService;
import lk.ijse.medihelpbackend.service.custom.UserService;
import lk.ijse.medihelpbackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    public AdminController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/reports/summary")
    public ResponseEntity<ResponseDTO> getDashboardSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();
            summary.put("doctorCount", userService.countUsersByRole("DOCTOR"));
            summary.put("patientCount", userService.countUsersByRole("USER"));
            summary.put("receptionistCount", userService.countUsersByRole("RECEPTION"));
            summary.put("totalAppointments", appointmentService.getAllAppointments().size());
            
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Summary data fetched", summary), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
