package lk.ijse.medihelpbackend.service.custom;

import lk.ijse.medihelpbackend.dto.UserDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String email);
    int updateUser(UserDTO userDTO);
    int deleteUser(String email);
    List<UserDTO> getAllUsers();
    List<UserDTO> getUsersByRole(String role);
    long countUsersByRole(String role);
}