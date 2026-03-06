package lk.ijse.medihelpbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private UUID   uid;
    private String email;
    private String name;
    private String password;
    private String role;
    private String national_id;
    private boolean verified;
    private String verificationCode;
    private Date joinDate;
}
