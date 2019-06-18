package test1;


import model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import repository.CustomerRepository;
import services.CustomerService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class Test1 {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName(" find all customers ")

    public void test1(){
        //GIVEN
        Mockito
                .when(customerRepository.findAll())
                .thenReturn(List.of(Customer.builder().id(1).name("ADAM").surname("KOWAL").age(22).email("adam.kowal@gmail.com").loyalty_card_id(null).build()));
        // WHEN
        List<Customer> customers = customerService.findAll();
        //THEN
        Assertions.assertEquals(1,customers.size());
    }


   /* @Test
    @DisplayName(" add person with no null arguments  ")
    public void test2(){
        // GIVEN

        Mockito
                .when(customerRepository.add(Customer.builder().name("ADAM").surname("KOWAL").age(22).email("adam.kowal@gmail.com").build())
                .thenReturn()



    }
    */



}