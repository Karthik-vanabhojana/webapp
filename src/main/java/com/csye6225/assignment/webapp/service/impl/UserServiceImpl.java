package com.csye6225.assignment.webapp.service.impl;

import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.entity.User;
import com.csye6225.assignment.webapp.entity.UserEmail;
import com.csye6225.assignment.webapp.exception.BadRequestEmail;
import com.csye6225.assignment.webapp.exception.DuplicateUserNameException;
import com.csye6225.assignment.webapp.exception.ResourceNotFoundException;
import com.csye6225.assignment.webapp.exception.UserNotverified;
import com.csye6225.assignment.webapp.repository.UserMailRepository;
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
    UserMailRepository userMailRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO registerUser(UserDTO userDto) throws DuplicateUserNameException {
        LOGGER.debug("UserServiceImpl. registerUser {} ");
        LOGGER.info("Creating the User.........");
        if(userRepository.findByEmail(userDto.getUsername()).isPresent()){
            throw new DuplicateUserNameException();
        }
        User user = this.convertDtoToEntity(userDto);

        user.setAccount_created(new Date());
        user.setAccount_updated(new Date());
        if (userDto.getPassword() != null) {
            String s = userDto.getPassword();
            user.setPassword(passwordEncoder.encode(s));

        }
        User newUser = this.userRepository.save(user);
        UserEmail userEmail = new UserEmail();
        userEmail.setId(newUser.getUserId());
        userEmail.setUser(newUser);
        userEmail.setMailVerified(false); // Assuming initial state
        userEmail.setEmailSent(false); // Assuming initial state
        userEmail.setMailSentTiming(new Date());


        userMailRepository.save(userEmail);

        LOGGER.info("User Created Sucessfully with username "+newUser.getEmail());

        return new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAccount_created(), user.getAccount_updated());
    }

    @Override
    public UserDTO updateUser(UserDTO userdto, String mail) throws BadRequestEmail, UserNotverified {
        LOGGER.debug("UserServiceImpl. updateUser {} ");
        LOGGER.info("Checking user with username " +mail);
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("UserName", " UserName", mail));
        UserEmail userEmail=this.userMailRepository.findByUser(user).orElseThrow(()->  new ResourceNotFoundException("Email", " Email Id", mail));
        LOGGER.info("Trying to update details");
        if(userEmail.isMailVerified()) {
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
            LOGGER.info("Updated details for user with username "+mail);


            return new UserDTO(newUser.getUserId(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), newUser.getAccount_created(), newUser.getAccount_updated());

        }

        else{
            throw new UserNotverified();
        }
    }

    @Override
    public UserDTO getuserByEmail(String mail) throws UserNotverified {
        LOGGER.debug("UserServiceImpl. getuserByEmail {} ");
        LOGGER.info("Checking user with username " +mail);
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Email", " Email Id", mail));
        UserEmail userEmail=this.userMailRepository.findByUser(user).orElseThrow(()->  new ResourceNotFoundException("Email", " Email Id", mail));
        LOGGER.info("Trying to update details");
        if(userEmail.isMailVerified()) {
            LOGGER.info("User with username "+mail+" found");

            return new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAccount_created(), user.getAccount_updated());

        }
        else{
            throw new UserNotverified();
        }
       }

    @Override
    public UserDTO getuser(String mail) {
        LOGGER.debug("UserServiceImpl. getuser {} ");
        LOGGER.info("Checking user with username " +mail);
        User user = this.userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Username", "Username", mail));
        LOGGER.info("User with username "+mail+" found");
        return this.convertEntityToDto(user);

    }

    public User convertDtoToEntity(UserDTO userDTO) {
        LOGGER.debug("UserServiceImpl. convertDtoToEntity {} ");
        LOGGER.debug("Converting DTO to Entity......");

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
        LOGGER.debug("UserServiceImpl. convertEntityToDto {} ");
        LOGGER.debug("Converting Entity to DTO......");


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
        LOGGER.debug("UserServiceImpl. checkDatabaseConnection {} ");

        LOGGER.info("Checking Connection......");

        try (Connection connection = dataSource.getConnection()) {
            LOGGER.info("Connection Sucessfully Established");
            return true;
        } catch (SQLException e) {
            LOGGER.error("Connection Failed");

            return false;
        }
    }

    @Override
    public void getVerified(String userId) {
        LOGGER.debug("UserServiceImpl. getVerified {} ");
        LOGGER.info("Verifying User......");


        User user = this.userRepository.findByEmail(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Name", " User Name", userId));
        LOGGER.info("Verifying User......");
        UserEmail userEmail=this.userMailRepository.findByUser(user).orElseThrow(()->  new ResourceNotFoundException("Email", " Email Id", userId));
        userEmail.setMailVerified(true);
        this.userMailRepository.save(userEmail);
        LOGGER.info("Verified Sucessfully with User Name: "+userId);

    }
}

