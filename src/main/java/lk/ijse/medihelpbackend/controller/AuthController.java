package lk.ijse.medihelpbackend.controller;

import lk.ijse.medihelpbackend.dto.AuthDTO;
import lk.ijse.medihelpbackend.dto.ResponseDTO;
import lk.ijse.medihelpbackend.dto.UserDTO;
import lk.ijse.medihelpbackend.service.custom.EmailService;
import lk.ijse.medihelpbackend.service.custom.impl.UserServiceImpl;
import lk.ijse.medihelpbackend.util.JwtUtil;
import lk.ijse.medihelpbackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final ResponseDTO responseDTO;

    @Autowired
    private EmailService emailService;

    // Store registration verification codes: email -> code
    private final ConcurrentHashMap<String, String> registerCodes = new ConcurrentHashMap<>();

    // Store password reset codes: email -> code
    private final ConcurrentHashMap<String, String> passwordResetCodes = new ConcurrentHashMap<>();

    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                          UserServiceImpl userService, ResponseDTO responseDTO) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.responseDTO = responseDTO;
    }

    // Direct login — returns JWT immediately on valid credentials
    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                new ResponseDTO(VarList.Unauthorized, "Invalid email or password", null),
                HttpStatus.UNAUTHORIZED
            );
        }

        UserDTO loadedUser = userService.loadUserDetailsByUsername(userDTO.getEmail());
        if (loadedUser == null) {
            return new ResponseEntity<>(
                new ResponseDTO(VarList.Not_Found, "User not found", null),
                HttpStatus.NOT_FOUND
            );
        }

        String token = jwtUtil.generateToken(loadedUser);
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(loadedUser.getEmail());
        authDTO.setToken(token);
        authDTO.setRole(loadedUser.getRole());

        return new ResponseEntity<>(
            new ResponseDTO(VarList.Created, "Login successful", authDTO),
            HttpStatus.CREATED
        );
    }

    // Send OTP code to email before registration
    @PostMapping("/send-register-code")
    public ResponseEntity<ResponseDTO> sendRegisterCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(new ResponseDTO(VarList.Bad_Request, "Email is required", null), HttpStatus.BAD_REQUEST);
        }

        // Check if email is already used
        if (userService.searchUser(email) != null) {
            return new ResponseEntity<>(
                new ResponseDTO(VarList.Not_Acceptable, "This email is already registered", null),
                HttpStatus.NOT_ACCEPTABLE
            );
        }

        String code = emailService.sendVerificationCode(email);
        registerCodes.put(email, code);

        return new ResponseEntity<>(
            new ResponseDTO(VarList.OK, "Verification code sent to " + email, null),
            HttpStatus.OK
        );
    }

    // Verify registration OTP code
    @PostMapping("/verify-register-code")
    public ResponseEntity<ResponseDTO> verifyRegisterCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");

        String stored = registerCodes.get(email);
        if (stored == null || !stored.equals(code)) {
            return new ResponseEntity<>(
                new ResponseDTO(VarList.Unauthorized, "Invalid or expired code", null),
                HttpStatus.UNAUTHORIZED
            );
        }
        registerCodes.remove(email);
        return new ResponseEntity<>(
            new ResponseDTO(VarList.OK, "Email verified successfully", null),
            HttpStatus.OK
        );
    }

    // Send password reset code
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        UserDTO user = userService.searchUser(email);
        if (user == null) {
            return new ResponseEntity<>(
                new ResponseDTO(VarList.Not_Found, "No account found with this email", null),
                HttpStatus.NOT_FOUND
            );
        }

        String code = String.format("%06d", new java.util.Random().nextInt(999999));
        passwordResetCodes.put(email, code);
        emailService.sendPasswordResetCode(email, code);

        return new ResponseEntity<>(
            new ResponseDTO(VarList.OK, "Password reset code sent to " + email, email),
            HttpStatus.OK
        );
    }

    // Verify reset code and update password
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        String newPassword = body.get("newPassword");

        String stored = passwordResetCodes.get(email);
        if (stored == null || !stored.equals(code)) {
            return new ResponseEntity<>(
                new ResponseDTO(VarList.Unauthorized, "Invalid or expired reset code", null),
                HttpStatus.UNAUTHORIZED
            );
        }

        passwordResetCodes.remove(email);

        UserDTO userDTO = userService.searchUser(email);
        userDTO.setPassword(newPassword);
        int result = userService.updateUser(userDTO);

        if (result == 200) {
            return new ResponseEntity<>(
                new ResponseDTO(VarList.OK, "Password updated successfully", null),
                HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
            new ResponseDTO(VarList.Internal_Server_Error, "Failed to update password", null),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
