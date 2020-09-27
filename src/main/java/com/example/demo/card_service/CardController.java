package com.example.demo.card_service;

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
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/banking/creditcard")
@ComponentScan
public class CardController {

    @Autowired
    private  CardService cardService;

    @Autowired
    private WorkerConfig workerService;

    @Autowired
    private UserConfig userService;

    final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards(){
        try{
            final List<CreditCard> userList = cardService.getAllCreditCards();

            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCard(@PathVariable("id") UUID cardId){

        try{
            final CreditCard user = cardService.getCardById(cardId);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCard(@Valid @RequestBody CardDataFormat cardBody){
        try{
            User owner = userService.getUserById(cardBody.ownerId);
            CreditCard newCard = new CreditCard(cardBody.regMonth, cardBody.regYear, cardBody.CVC, cardBody.cardNum, owner);

            Worker worker = workerService.getWorkerById(cardBody.workerId);
            worker.setWage(worker.getWage() + 1000);

            cardService.openCard(newCard);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value="/change_balance", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeCreditCardBalance(@Valid @RequestBody CardOperationDataFormat cardBody) throws NotFoundException {
        Worker worker = workerService.getWorkerById(cardBody.workerId);
        worker.setWage(worker.getWage() + 1000);
        User owner = userService.getUserById(cardService.getCardById(cardBody.cardId).getOwnerId());
        if (cardBody.balanceChange > 0){
                owner.setUserBalance(owner.getUserBalance() - cardBody.balanceChange);
                cardService.putMoney(cardBody.cardId, cardBody.balanceChange);
            }
        else {
            if (cardService.getCardBalance(cardBody.cardId) >= -1*cardBody.balanceChange){
                owner.setUserBalance(owner.getUserBalance() + -1*cardBody.balanceChange);
                cardService.withdrawMoney(cardBody.cardId, cardBody.balanceChange);
            }

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> closeCard(@RequestBody CardCloseDataFormat cardCloseBody) throws NotFoundException {
        cardService.closeCC(cardCloseBody.cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}