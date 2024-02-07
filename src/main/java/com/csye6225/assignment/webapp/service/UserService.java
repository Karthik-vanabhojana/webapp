package com.csye6225.assignment.webapp.service;

import com.csye6225.assignment.webapp.dto.UserDTO;

public interface UserService {
    UserDTO registerUser(UserDTO user) ;
    UserDTO updateUser(UserDTO user, String email) ;
    UserDTO getuserByEmail(String mail);
    UserDTO getuser(String mail);
     boolean checkDatabaseConnection();
}
