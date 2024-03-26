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
@Table(name = "UserEmail")

public class UserEmail {
    @Id
    @Type(type = "uuid-char")
    @Column(updatable = false, nullable = false,name="id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "is_email_sent")
    private boolean isEmailSent;

    @Column(name = "is_mail_verified")
    private boolean isMailVerified;

    @Column(name = "mail_sent_timing")
    private Date mailSentTiming;

    @Column(name = "token")
    private String token;

}
