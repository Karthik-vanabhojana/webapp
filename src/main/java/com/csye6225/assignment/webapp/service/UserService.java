package com.csye6225.assignment.webapp.service;

import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.exception.BadRequestEmail;
import com.csye6225.assignment.webapp.exception.DuplicateUserNameException;

public interface UserService {
    UserDTO registerUser(UserDTO user) ;
    UserDTO updateUser(UserDTO user, String email) throws BadRequestEmail;
    UserDTO getuserByEmail(String mail);
    UserDTO getuser(String mail);
     boolean checkDatabaseConnection();
}
