package com.example.demo.api;

import com.example.demo.DemoApplication;
import com.example.demo.services.deposit_service.model.DepositAccount;
import com.example.demo.dto.DepositDataFormat;
import com.example.demo.services.deposit_service.DepositService;
import com.example.demo.dto.DepositWithdrawDataFormat;
import com.example.demo.services.user_service.model.User;
import com.example.demo.services.user_service.UserConfig;
import com.example.demo.services.worker_service.model.Worker;
import com.example.demo.services.worker_service.WorkerConfig;
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

            Date openingDate = new SimpleDateFormat("yyyy-MM-dd").parse(depositBody.getOpeningDate());
            User owner = userService.getUserById(depositBody.getOwnerId());
            if (owner.getUserBalance() < depositBody.getBalance()) throw new IllegalArgumentException(
                    "Недостаточно средств для открытия депозита"
            );
            owner.setUserBalance(owner.getUserBalance() - depositBody.getBalance());

            Worker worker = workerService.getWorkerById(depositBody.getWorkerId());
            worker.setWage(worker.getWage() + 1000);

            DepositAccount newDeposit = new DepositAccount(depositBody.getBalance(), openingDate,
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
        Date withdrawDate = new SimpleDateFormat("yyyy-MM-dd").parse(depositBody.getWithdrawDate());

        DepositAccount deposit = depositService.getDepositById(depositBody.getDepositId());
        User owner = userService.getUserById(deposit.getOwnerId());
        Worker exec = workerService.getWorkerById(depositBody.getWorkerId());
        exec.setWage(exec.getWage() + 1000);


        int withdrawMoney = depositService.getWithdrawMoney(depositBody.getDepositId(), withdrawDate);
        System.out.println("User current balance:" + owner.getUserBalance());
        owner.setUserBalance(owner.getUserBalance() + withdrawMoney);
        System.out.println("User current balance after withdrawal:" + owner.getUserBalance());
        depositService.closeDeposit(depositBody.getDepositId());


        return new ResponseEntity<>(HttpStatus.OK);

    }
}