package com.csye6225.assignment.webapp.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "User",indexes = {
        @Index(name = "index_username",columnList = "username", unique = true)
})
public class User  {
    @Id
    @Type(type = "uuid-char")
    @Column(updatable = false, nullable = false,name="id")
    private UUID userId;
    @Column(name="username", nullable = false, updatable = false,length = 70, unique = true)
    private String email;
    @Column(name="password", nullable = false, length = 70)
    private String password;
    @Column(name="first_name", nullable = false, length = 70)
    private String firstName;
    @Column(name="last_name", nullable = false, length = 70)
    private String lastName;
    private Date account_created;
    private Date  account_updated;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserEmail userEmail;

}
