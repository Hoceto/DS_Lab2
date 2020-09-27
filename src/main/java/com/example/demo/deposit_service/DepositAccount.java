package com.example.demo.deposit_service;

import com.example.demo.user_service.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="deposits")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EnableAutoConfiguration
public final class DepositAccount {

    @Id
    @GeneratedValue
    @Column(name="deposit_id")
    private UUID depositId;

    @Column(name="balance")
    private int balance;
    @Column(name="opening_date")
    private Date openingDate;

    @OneToOne//(mappedBy = "userDeposit", cascade = CascadeType.ALL)
    @JoinColumn(name="owner_id")
    private User owner;

    public DepositAccount(int balance, Date openingDate, User owner) {
        this.balance = balance;
        this.openingDate = openingDate;
        this.owner = owner;
    }

    void setBalance(int money) {
        this.balance = money;
    }

    public int getBalance() {
        return this.balance;
    }

    public Date getOpeningDate() {
        return this.openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public UUID getDepositId() {
        return this.depositId;
    }

    void setDepositId() {
        this.depositId = UUID.randomUUID();
    }

    User getOwner() {
        return this.owner;
    }

    void setOwner(User owner) {
        this.owner = owner;
    }

    public UUID getOwnerId() {
        return owner.getUserId();
    }

    double calculateMultiplier(Date withdraw_date) {
        Calendar cal = Calendar.getInstance();
        if (!this.openingDate.before(withdraw_date)) {
            throw new IllegalArgumentException("Wrong withdraw data (must always be after opening");
        } else {
            cal.setTime(this.openingDate);

            int c;
            for(c = 0; cal.getTime().before(withdraw_date); ++c) {
                cal.add(2, 1);
            }

            double percent = 0.095D;
            return Math.pow(1.0D + percent, (double)(c - 1));
        }
    }

    int withdrawBalance(Date withdraw_date) {
        return (int)(this.balance * this.calculateMultiplier(withdraw_date));
    }
}