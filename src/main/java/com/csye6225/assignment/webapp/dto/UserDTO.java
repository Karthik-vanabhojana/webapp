package com.csye6225.assignment.webapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private UUID id;
    @Size(message = "password Should Not be Empty", min=2, max = 40,groups = {CreateValid.class, UpdateValid.class})
    private String password;
    @Size(message = "first_name Should Not be Empty", min=2, max = 40,groups = {CreateValid.class, UpdateValid.class})
    private String first_name;
    @Size(message = "last_name Should Not be Empty", min=2, max = 40,groups = {CreateValid.class, UpdateValid.class})
    private String last_name;
    @Size(message = "Username Should Not be Empty",  min=2, max = 40,groups = {CreateValid.class})
    @Email(message = "Your Username Address is not Valid !!",groups = {CreateValid.class})
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message ="Your Username Address is not Valid !!" )
    private String username;
    private Date account_created;
    private Date account_updated;
    public UserDTO(UUID id, String username, String first_name, String last_name, Date account_created, Date account_updated) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.account_created = account_created;
        this.account_updated = account_updated;
    }

}
