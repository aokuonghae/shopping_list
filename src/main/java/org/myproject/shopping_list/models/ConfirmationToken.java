package org.myproject.shopping_list.models;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken extends AbstractEntity{
    @Column(name="confirmation_token")
    private String confirmationToken;

    private static final int EXPIRATION=60*24;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity=User.class, fetch=FetchType.EAGER)
    private User user;

    private Date expiryDate;

    public ConfirmationToken(){}

    public ConfirmationToken(User user){
        this.user=user;
        this.createdDate= new Date();
        this.expiryDate= calculateExpiryDate(EXPIRATION);
        confirmationToken= UUID.randomUUID().toString();
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateToken(final String token){
        this.confirmationToken=token;
        this.createdDate=new Date();
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
