package com.example.demo.services.deposit_service;

import com.example.demo.services.deposit_service.model.DepositAccount;
import com.example.demo.services.deposit_service.repository.DepositRepository;
import com.example.demo.services.user_service.model.User;
import com.example.demo.services.user_service.UserInterface;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class DepositService {

    @Autowired
    private DepositRepository repository;

    @Autowired
    private UserInterface userService;


    public DepositAccount openDeposit(UUID userId, Date openingDate, int depositMoney) throws NotFoundException {
        User owner = userService.getUserById(userId);
        DepositAccount deposit = new DepositAccount(depositMoney, openingDate, owner);
        return repository.save(deposit);
    }

    public DepositAccount addDeposit(DepositAccount newDeposit){
        return repository.save(newDeposit);
    }

    public List<DepositAccount> getAllDeposits(){
        List<DepositAccount> depositList = (List<DepositAccount>) repository.findAll();

        return depositList;
    }


    public DepositAccount getDepositById(UUID depositID) throws NotFoundException {
        Optional<DepositAccount> deposit = repository.findById(depositID);
        if (deposit.isPresent()){
            return deposit.get();
        }
        else {
            throw new NotFoundException(String.format("Deposit ID %s does not exist", depositID));
        }
    }

    public void closeDeposit(UUID depositID) throws NotFoundException {
        DepositAccount toRemove = getDepositById(depositID);
        repository.delete(toRemove);
    }

    public int getWithdrawMoney(UUID depositId, Date withdrawDate) throws NotFoundException {
        int withdrawValue = getDepositById(depositId).withdrawBalance(withdrawDate);
        return withdrawValue;
    }

}