package com.csye6225.assignment.webapp.service.impl;

import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.entity.User;
import com.csye6225.assignment.webapp.exception.BadRequestEmail;
import com.csye6225.assignment.webapp.exception.ResourceNotFoundException;
import com.csye6225.assignment.webapp.repository.UserRepository;
import com.csye6225.assignment.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO registerUser(UserDTO userDto) {
        LOGGER.trace("UserServiceImpl. registerUser {} ");
        LOGGER.info("Creating the User.........");
        User user = this.convertDtoToEntity(userDto);

        user.setAccount_created(new Date());
        user.setAccount_updated(new Date());
        if (userDto.getPassword() != null) {
            String s = userDto.getPassword();
            user.setPassword(passwordEncoder.encode(s));

        }
        User newUser = this.userRepository.save(user);
        LOGGER.info("User Created Sucessfully with user name "+newUser.getUserId());

        return new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAccount_created(), user.getAccount_updated());
    }

    @Override
    public UserDTO updateUser(UserDTO userdto, String mail) throws BadRequestEmail {
        LOGGER.trace("UserServiceImpl. updateUser {} ");
        LOGGER.info("Checking user with mail Id " +mail);
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Email", " Email Id", mail));
        LOGGER.info("Trying to update details");

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
        LOGGER.info("Updated details for user with email "+mail);


        return new UserDTO(newUser.getUserId(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), newUser.getAccount_created(), newUser.getAccount_updated());

    }

    @Override
    public UserDTO getuserByEmail(String mail) {
        LOGGER.trace("UserServiceImpl. getuserByEmail {} ");
        LOGGER.info("Checking user with mail Id " +mail);
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Email", " Email Id", mail));
        LOGGER.info("User with mail "+mail+" found");

        return new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAccount_created(), user.getAccount_updated());
    }

    @Override
    public UserDTO getuser(String mail) {
        LOGGER.trace("UserServiceImpl. getuser {} ");
        LOGGER.info("Checking user with mail Id " +mail);
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Email", " Email Id", mail));
        LOGGER.info("User with mail "+mail+" found");
        return this.convertEntityToDto(user);

    }

    public User convertDtoToEntity(UserDTO userDTO) {
        LOGGER.trace("UserServiceImpl. convertDtoToEntity {} ");
        LOGGER.info("Converting DTO to Entity......");
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
        LOGGER.trace("UserServiceImpl. convertEntityToDto {} ");
        LOGGER.info("Converting Entity to DTO......");

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
        LOGGER.trace("UserServiceImpl. checkDatabaseConnection {} ");

        LOGGER.info("Checking Connection......");

        try (Connection connection = dataSource.getConnection()) {
            LOGGER.info("Connection Sucessfully Established");
            return true;
        } catch (SQLException e) {
            LOGGER.error("Connection Failed");

            return false;
        }
    }
}
