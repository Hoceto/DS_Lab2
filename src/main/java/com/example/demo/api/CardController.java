package com.example.demo.api;

import com.example.demo.DemoApplication;
import com.example.demo.dto.CardCloseDataFormat;
import com.example.demo.dto.CardDataFormat;
import com.example.demo.dto.CardOperationDataFormat;
import com.example.demo.services.card_service.CardService;
import com.example.demo.services.card_service.model.CreditCard;
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
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/banking/creditcard")
@ComponentScan
public class CardController {

    @Autowired
    private CardService cardService;

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
            User owner = userService.getUserById(cardBody.getOwnerId());
            CreditCard newCard = new CreditCard(cardBody.getRegMonth(), cardBody.getRegYear(), cardBody.getCVC(),
                    cardBody.getCardNum(), owner);

            Worker worker = workerService.getWorkerById(cardBody.getWorkerId());
            worker.setWage(worker.getWage() + 1000);

            cardService.openCard(newCard);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value="/change_balance", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeCreditCardBalance(@Valid @RequestBody CardOperationDataFormat cardBody) throws NotFoundException {
        Worker worker = workerService.getWorkerById(cardBody.getWorkerId());
        worker.setWage(worker.getWage() + 1000);
        User owner = userService.getUserById(cardService.getCardById(cardBody.getCardId()).getOwnerId());
        if (cardBody.getBalanceChange() > 0){
                owner.setUserBalance(owner.getUserBalance() - cardBody.getBalanceChange());
                cardService.putMoney(cardBody.getCardId(), cardBody.getBalanceChange());
            }
        else {
            if (cardService.getCardBalance(cardBody.getCardId()) >= -1*cardBody.getBalanceChange()){
                owner.setUserBalance(owner.getUserBalance() + -1*cardBody.getBalanceChange());
                cardService.withdrawMoney(cardBody.getCardId(), cardBody.getBalanceChange());
            }

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> closeCard(@RequestBody CardCloseDataFormat cardCloseBody) throws NotFoundException {
        cardService.closeCC(cardCloseBody.getCardId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}