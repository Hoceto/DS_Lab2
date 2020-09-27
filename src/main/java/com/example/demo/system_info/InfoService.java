package com.example.demo.system_info;

import com.example.demo.card_service.CreditCard;
import com.example.demo.deposit_service.DepositAccount;
import com.example.demo.user_service.User;
import com.example.demo.worker_service.Worker;
import java.util.Date;
import java.util.UUID;

public final class InfoService {
    public InfoService() {
    }

//    public String depositOpenInfo(User user, DepositAccount deposit) {
//        Object[] var10001 = new Object[]{deposit.getDepositId(), user.getUserName(), user.getUserSurname(), deposit.getDateFormat().format(deposit.getOpeningDate()), deposit.getBalance()};
//        return "--------------------------Открытие депозита--------------------------------\n" + String.format("Новый депозит открыт. ID: %s. Владелец: %s %s, дата открытия: %s, баланс:%d\n", var10001) + "--------------------------------------------------------------------------\n";
//    }
//
//    public String depositWithdrawInfo(User user, DepositAccount deposit, Date withdrawDate, int withdrawMoney) {
//        Object[] var10001 = new Object[]{deposit.getDepositId(), user.getUserName(), user.getUserSurname(), deposit.getDateFormat().format(withdrawDate), withdrawMoney};
//        return "\n----------------------------Закрытие депозита----------------------------\n" + String.format("Депозит %s закрыт. Владелец %s %s, дата закрытия: %s, выведено со счета: %d\n", var10001) + "--------------------------------------------------------------------------\n";
//    }
//
//    public String userRegistrationInfo(User user) {
//        Object[] var10001 = new Object[]{user.getUserId(), user.getUserName(), user.getUserSurname(), user.getUserEmail(), user.getUserPhoneNumber()};
//        return "\n----------------------------Регистрация пользователя---------------------\n" + String.format("Зарегистрирован новый пользователь. UUID: %s\n  -Имя: %s %s\n  -Почта: %s,\n  -Тел. номер: %s\n", var10001) + "--------------------------------------------------------------------------\n";
//    }
//
//    public String userRemoveInfo(UUID userId) {
//        Object[] var10001 = new Object[]{userId};
//        return "----------------------------Удаление пользователя--------------------------\n" + String.format("Пользователь %s был удален\n", var10001) + "--------------------------------------------------------------------------\n";
//    }
//
//    public String workerRegistrationInfo(Worker worker) {
//        Object[] var10001 = new Object[]{worker.getWorkerId(), worker.getWorkerName(), worker.getWorkerSurname(), worker.getPositionName()};
//        return "---------------------------Регистрация работника---------------------------\n" + String.format("Зарегистрирован новый работник. ID: %s\n  -Имя: %s %s\n  -Должность: %s\n", var10001) + "--------------------------------------------------------------------------\n";
//    }
//
//    public String workerRemoveInfo(UUID workerId) {
//        Object[] var10001 = new Object[]{workerId};
//        return "---------------------------Удаление работника------------------------------\n" + String.format("Работник %s был удален\n", var10001) + "--------------------------------------------------------------------------\n";
//    }
//
//    public String CCOpenInfo(CreditCard card) {
//        Object[] var10001 = new Object[]{card.getCardId(), card.getOwnerId(), card.getCcNum(), card.getExpMonth(), card.getExpYear(), card.getCVC()};
//        return "---------------------------Открытие кред. карты----------------------------\n" + String.format("Открыта новая кредитная карта. ID: %s\n -Владелец: %s\n -Данные карты: %s %s/%s %s \n", var10001) + "--------------------------------------------------------------------------\n";
//    }
//
//    public String CCBalanceChange(UUID cardId, int changeAmount, boolean delta, int remainBalance) {
//        Object[] var10001 = new Object[]{cardId, String.format("%s%d", delta ? "+" : "-", changeAmount), remainBalance};
//        return "-----------------------Изменение баланса кред. карты-----------------------\n" + String.format("Кред. карта ID: %s\n---измен. баланса: %s, остаток: %d\n", var10001) + "--------------------------------------------------------------------------\n";
//    }
}
