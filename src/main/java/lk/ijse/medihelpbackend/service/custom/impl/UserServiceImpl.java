package lk.ijse.medihelpbackend.service.custom.impl;

import lk.ijse.medihelpbackend.Entity.User;
import lk.ijse.medihelpbackend.dto.UserDTO;
import lk.ijse.medihelpbackend.repo.UserRepository;
import lk.ijse.medihelpbackend.service.custom.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            UserDTO admin = new UserDTO();
            admin.setName("System Admin");
            admin.setEmail("admin@medihelp.com");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            admin.setVerified(true);
            saveUser(admin);
            System.out.println("Default Admin created: admin@medihelp.com / admin123");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return modelMapper.map(user, UserDTO.class);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Override
    public int saveUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return 406;
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setJoinDate(new java.sql.Date(System.currentTimeMillis()));
        userRepository.save(user);
        return 201;
    }

    @Override
    public UserDTO searchUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public int updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            user.setName(userDTO.getName());
            user.setNationalId(userDTO.getNational_id());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            }
            userRepository.save(user);
            return 200;
        }
        return 404;
    }

    @Override
    public int deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return 200;
        }
        return 404;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
    }

    @Override
    public List<UserDTO> getUsersByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        return modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
    }

    @Override
    public long countUsersByRole(String role) {
        return userRepository.countByRole(role);
    }
}
