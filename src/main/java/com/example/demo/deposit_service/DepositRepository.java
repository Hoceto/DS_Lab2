package com.example.demo.deposit_service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.deposit_service.DepositAccount;

import java.util.UUID;

@Repository
public interface DepositRepository extends CrudRepository<DepositAccount, UUID> {

}
