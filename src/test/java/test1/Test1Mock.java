package test1;

import exception.AppException;
import model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import repository.CustomerRepository;
import services.CustomerService;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class Test1Mock {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName(" find all customers ")
    public void test1() {
        //GIVEN
        Mockito
                .when(customerRepository.findAll())
                .thenReturn(List.of(Customer.builder().name("ADAM").surname("KOWAL").age(22).email("adam.kowal@gmail.com").build()));

        // WHEN
        List<Customer> customers = customerService.findAll();

        //THEN
        Assertions.assertEquals(1, customers.size());
    }

    @Test
    @DisplayName(" add person null ")
    public void test2() {

        AppException e = Assertions.assertThrows(AppException.class, () -> customerService.addCustomer(null));
        Assertions.assertEquals("customer is null", e.getMessage());

    }

    @Test
    @DisplayName("find customer by id number")
    public void test3() {
        // GIVEN
        Mockito
                .when(customerRepository.findOne(1))
                .thenReturn(java.util.Optional.ofNullable(Customer.builder().name("ADAM").surname("KOWAL").age(22).email("adam.kowal@gmail.com").build()));
        // WHEN
        Customer customer = (customerService.getCustomerById(1)).get();
        // THEN
        Assertions.assertTrue(customer.getAge() == 22);

    }

    @Test
    @DisplayName(" delete person with null id ")
    public void test4() {

        AppException e = Assertions.assertThrows(AppException.class, () -> customerService.removeCustomerById(null));
        Assertions.assertEquals("null id number", e.getMessage());
    }

}




