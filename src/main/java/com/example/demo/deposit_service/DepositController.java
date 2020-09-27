package com.example.demo.deposit_service;

import com.example.demo.DemoApplication;
import com.example.demo.user_service.User;
import com.example.demo.user_service.UserConfig;
import com.example.demo.worker_service.Worker;
import com.example.demo.worker_service.WorkerConfig;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/banking/deposit")
@ComponentScan
public class DepositController {

    @Autowired
    private DepositService depositService;

    @Autowired
    private UserConfig userService;

    @Autowired
    private WorkerConfig workerService;

    final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @GetMapping
    public ResponseEntity<List<DepositAccount>> getAllDeposits(){
        try{
            final List<DepositAccount> depositList = depositService.getAllDeposits();

            return new ResponseEntity<>(depositList, HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<DepositAccount> getDeposit(@PathVariable("id") UUID depositId){

        try{
            final DepositAccount user = depositService.getDepositById(depositId);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createDeposit(@Valid @RequestBody DepositDataFormat depositBody) throws ParseException {
        try{

            Date openingDate = new SimpleDateFormat("yyyy-MM-dd").parse(depositBody.openingDate);
            User owner = userService.getUserById(depositBody.ownerId);
            if (owner.getUserBalance() < depositBody.balance) throw new IllegalArgumentException(
                    "Недостаточно средств для открытия депозита"
            );
            owner.setUserBalance(owner.getUserBalance() - depositBody.balance);

            Worker worker = workerService.getWorkerById(depositBody.workerId);
            worker.setWage(worker.getWage() + 1000);

            DepositAccount newDeposit = new DepositAccount(depositBody.balance, openingDate,
            owner);
            depositService.addDeposit(newDeposit);
            owner.setUserDeposit(newDeposit);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<Void> withdrawDeposit(@Valid @RequestBody DepositWithdrawDataFormat depositBody) throws NotFoundException, ParseException {
        Date withdrawDate = new SimpleDateFormat("yyyy-MM-dd").parse(depositBody.withdrawDate);

        DepositAccount deposit = depositService.getDepositById(depositBody.depositId);
        User owner = userService.getUserById(deposit.getOwnerId());
        Worker exec = workerService.getWorkerById(depositBody.workerId);
        exec.setWage(exec.getWage() + 1000);


        int withdrawMoney = depositService.getWithdrawMoney(depositBody.depositId, withdrawDate);
        System.out.println("User current balance:" + owner.getUserBalance());
        owner.setUserBalance(owner.getUserBalance() + withdrawMoney);
        System.out.println("User current balance after withdrawal:" + owner.getUserBalance());
        depositService.closeDeposit(depositBody.depositId);


        return new ResponseEntity<>(HttpStatus.OK);

    }
}