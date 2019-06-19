package test1;

import exception.AppException;
import model.Customer;
import org.junit.Test;
import valid.CustomerValidator;


public class UserValidatorTest {

    private CustomerValidator customerValidator = CustomerValidator.getInstance();

    @Test
    public void shouldNowThrowAnyExceptionWhileValidationCorrectCustomer(){
        // given
        Customer customer = Customer.builder().name("ADAM").surname("KOWAL").age(22).email("adam.kowal@gmail.com").build();
        // when
        customerValidator.isValidate(customer);
    }

    @Test(expected = AppException.class)
    public void shouldThrowExceptionWhileValidationCustomerWithTooShortLogin(){
        // given
        Customer customer = Customer.builder().name("AS").surname("UL").age(22).email("adam.kowal@gmail.com").build();
        //when
        customerValidator.isValidate(customer);

    }

    @Test(expected = AppException.class)
    public void shouldThrowExceptionWhileValidationCustomerWithWrongAge(){
        //given
        Customer customer = Customer.builder().name("AS").surname("UL").age(22).email("adam.kowal@gmail.com").build();
        // when
        customerValidator.isValidate(customer);
    }
}
