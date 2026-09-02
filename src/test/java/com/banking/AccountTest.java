package com.banking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {
    Account account;
    Customer test1;
    @BeforeEach
    void setup(){
         test1 = new Customer(1, "test1","test1@gmail.com", "1234567890");
         account = new Account("8888800001", new BigDecimal("1000"), AccountType.SAVINGS, test1);
    }
    @Test
   public void shouldWithdrawAmountSuccessfully(){

        account.withdraw(new BigDecimal("500"));
       assertEquals( new BigDecimal("500"), account.getBalance()
       );
    }
    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        InsufficientBalanceException exception =assertThrows( InsufficientBalanceException.class,() -> account.withdraw(new BigDecimal("1500")));
        assertEquals("Insufficient Balance !", exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenBalanceIsZero(){
        InvalidAmountException exception =assertThrows(InvalidAmountException.class, ()->account.withdraw(BigDecimal.ZERO));
        assertEquals("Withdrawal amount must be greater than zero!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsNegative(){
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, ()->account.withdraw(new BigDecimal("-500")));
        assertEquals("Withdrawal amount must be greater than zero!", exception.getMessage());
    }

    @Test
    void shouldDepositAmountSuccessfully(){
        account.deposit(new BigDecimal("500"));
        assertEquals( new BigDecimal("1500"), account.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsNegativeInDeposit(){
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, ()->account.deposit(new BigDecimal("-500")));
        assertEquals("Deposit amount must be greater than zero!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsZeroInDeposit(){
        InvalidAmountException exception =assertThrows(InvalidAmountException.class, ()->account.deposit( BigDecimal.ZERO));
        assertEquals("Deposit amount must be greater than zero!", exception.getMessage());
    }

}
