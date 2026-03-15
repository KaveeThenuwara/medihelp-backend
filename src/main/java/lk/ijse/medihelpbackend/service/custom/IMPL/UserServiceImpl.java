package lk.ijse.medihelpbackend.service.custom.IMPL;



import lk.ijse.medihelpbackend.Entity.User;
import lk.ijse.medihelpbackend.dto.UserDTO;
import lk.ijse.medihelpbackend.repo.UserRepository;
import lk.ijse.medihelpbackend.service.custom.UserService;
import lk.ijse.medihelpbackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {
  /*  @Autowired
    private MemberRepository memberRepository;*/
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return modelMapper.map(user,UserDTO.class);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Override
    public int saveUser(UserDTO userDTO) {
        System.out.println("user save detail come from controller");
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            return VarList.All_Ready_Added;
        }
        //password hash
        String password = userDTO.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        userDTO.setPassword(encodedPassword);
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);
        return VarList.Created;
    }

    @Override
    public UserDTO searchUser(String username) {
        return null;
    }


    //get last 4 users


};

