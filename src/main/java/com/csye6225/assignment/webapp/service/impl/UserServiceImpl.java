package com.csye6225.assignment.webapp.service.impl;

import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.entity.User;
import com.csye6225.assignment.webapp.exception.BadRequestEmail;
import com.csye6225.assignment.webapp.exception.ResourceNotFoundException;
import com.csye6225.assignment.webapp.repository.UserRepository;
import com.csye6225.assignment.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO registerUser(UserDTO userDto) {
        User user = this.convertDtoToEntity(userDto);

        user.setAccount_created(new Date());
        user.setAccount_updated(new Date());
        if (userDto.getPassword() != null) {
            String s = userDto.getPassword();
            user.setPassword(passwordEncoder.encode(s));

        }
        User newUser = this.userRepository.save(user);

        return new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAccount_created(), user.getAccount_updated());
    }

    @Override
    public UserDTO updateUser(UserDTO userdto, String mail) throws BadRequestEmail {
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Email", " Email Id", mail));
        if (userdto.getPassword() != null) {
            String s = userdto.getPassword();
            user.setPassword(passwordEncoder.encode(s));

        }
        if (userdto.getFirst_name() != null) {
            user.setFirstName(userdto.getFirst_name());

        }
        if (userdto.getLast_name() != null) {
            user.setLastName(userdto.getLast_name());
        }
        if(userdto.getLast_name() == null && userdto.getFirst_name() == null && userdto.getPassword() == null){
            throw new BadRequestEmail();
        }
        user.setAccount_updated(new Date());
        User newUser = this.userRepository.save(user);


        return new UserDTO(newUser.getUserId(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), newUser.getAccount_created(), newUser.getAccount_updated());

    }

    @Override
    public UserDTO getuserByEmail(String mail) {
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Email", " Email Id", mail));
        return new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAccount_created(), user.getAccount_updated());
    }

    @Override
    public UserDTO getuser(String mail) {
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Email", " Email Id", mail));
        return this.convertEntityToDto(user);

    }

    public User convertDtoToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirst_name());
        user.setLastName(userDTO.getLast_name());
        user.setAccount_created(userDTO.getAccount_created());
        user.setAccount_updated(userDTO.getAccount_updated());
        return user;
    }

    public UserDTO convertEntityToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserId());
        userDTO.setPassword(user.getPassword());
        userDTO.setUsername(user.getEmail());
        userDTO.setFirst_name(user.getFirstName());
        userDTO.setLast_name(user.getLastName());
        userDTO.setAccount_created(user.getAccount_created());
        userDTO.setAccount_updated(user.getAccount_updated());
        return userDTO;
    }

    @Override
    public boolean checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
