package lk.ijse.medihelpbackend.service.custom;



import lk.ijse.medihelpbackend.dto.UserDTO;


public interface UserService {
    //get last 4 users
    int saveUser(UserDTO userDTO);


    UserDTO searchUser(String username);



}