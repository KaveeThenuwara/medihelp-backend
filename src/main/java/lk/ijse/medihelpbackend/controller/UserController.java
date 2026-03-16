package lk.ijse.medihelpbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.medihelpbackend.dto.AuthDTO;
import lk.ijse.medihelpbackend.dto.ResponseDTO;
import lk.ijse.medihelpbackend.dto.UserDTO;
import lk.ijse.medihelpbackend.service.custom.UserService;
import lk.ijse.medihelpbackend.util.JwtUtil;
import lk.ijse.medihelpbackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lk.ijse.medihelpbackend.service.custom.EmailService;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @org.springframework.beans.factory.annotation.Autowired
    private EmailService emailService;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            int res = userService.saveUser(userDTO);
            if (res == VarList.Created) {
                String token = jwtUtil.generateToken(userDTO);
                AuthDTO authDTO = new AuthDTO();
                authDTO.setEmail(userDTO.getEmail());
                authDTO.setToken(token);
                authDTO.setRole(userDTO.getRole());
                // Send welcome email
                try { emailService.sendWelcomeEmail(userDTO.getEmail(), userDTO.getName() != null ? userDTO.getName() : "there"); } catch (Exception ignored) {}
                return new ResponseEntity<>(new ResponseDTO(VarList.Created, "User registered successfully", authDTO), HttpStatus.CREATED);
            } else if (res == VarList.Not_Acceptable) {
                return new ResponseEntity<>(new ResponseDTO(VarList.Not_Acceptable, "Email already used", null), HttpStatus.NOT_ACCEPTABLE);
            } else {
                return new ResponseEntity<>(new ResponseDTO(VarList.Bad_Gateway, "Internal Error", null), HttpStatus.BAD_GATEWAY);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateUser(@RequestBody UserDTO userDTO) {
        try {
            int res = userService.updateUser(userDTO);
            if (res == VarList.OK) {
                return new ResponseEntity<>(new ResponseDTO(VarList.OK, "User updated successfully", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseDTO(VarList.Not_Found, "User not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable String email) {
        try {
            int res = userService.deleteUser(email);
            if (res == VarList.OK) {
                return new ResponseEntity<>(new ResponseDTO(VarList.OK, "User deleted successfully", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseDTO(VarList.Not_Found, "User not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{email}")
    public ResponseEntity<ResponseDTO> searchUser(@PathVariable String email) {
        try {
            UserDTO userDTO = userService.searchUser(email);
            if (userDTO != null) {
                return new ResponseEntity<>(new ResponseDTO(VarList.OK, "User found", userDTO), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseDTO(VarList.Not_Found, "User not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "All users fetched", users), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ResponseDTO> getUsersByRole(@PathVariable String role) {
        try {
            List<UserDTO> users = userService.getUsersByRole(role);
            return new ResponseEntity<>(new ResponseDTO(VarList.OK, "Users by role fetched", users), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
