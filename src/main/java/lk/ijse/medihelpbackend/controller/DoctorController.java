package lk.ijse.medihelpbackend.controller;

import lk.ijse.medihelpbackend.dto.DoctorDTO;
import lk.ijse.medihelpbackend.dto.ResponseDTO;
import lk.ijse.medihelpbackend.service.custom.DoctorService;
import lk.ijse.medihelpbackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/doctors")
@CrossOrigin
public class DoctorController {

    @Autowired 
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() { 
        try {
            List<DoctorDTO> doctors = doctorService.getAllDoctors();
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Success", doctors), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseDTO> save(@RequestBody DoctorDTO dto) { 
        try {
            DoctorDTO savedDoctor = doctorService.saveDoctor(dto);
            if (savedDoctor != null) {
                return new ResponseEntity<>(new ResponseDTO(VarList.Created, "Doctor created successfully", savedDoctor), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseDTO(VarList.Not_Found, "User not found for the given email", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{email}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String email) { 
        try {
            doctorService.deleteDoctor(email);
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Doctor deleted successfully", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
