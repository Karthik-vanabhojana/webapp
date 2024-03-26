package com.csye6225.assignment.webapp.service;

import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.exception.BadRequestEmail;
import com.csye6225.assignment.webapp.exception.DuplicateUserNameException;
import com.csye6225.assignment.webapp.exception.UserNotverified;

public interface UserService {
    UserDTO registerUser(UserDTO user) throws DuplicateUserNameException;
    UserDTO updateUser(UserDTO user, String email) throws BadRequestEmail, UserNotverified;
    UserDTO getuserByEmail(String mail) throws UserNotverified;
    UserDTO getuser(String mail);
     boolean checkDatabaseConnection();

    boolean getVerified(String userId) throws UserNotverified;
}
