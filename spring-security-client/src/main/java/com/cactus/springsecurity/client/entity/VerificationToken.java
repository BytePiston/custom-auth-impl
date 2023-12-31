package com.cactus.springsecurity.client.entity;

import com.cactus.springsecurity.client.utils.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationTime();
    }

    public VerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationTime();
    }

    private Date calculateExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, Constants.EXPIRATION_TIME_IN_MINUTES);
        return calendar.getTime();
    }
}
