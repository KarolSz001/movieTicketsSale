package api;

import model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    void addCustomer(Customer customer);
    void removeCustomerById(Integer id);

    List<Customer> getAllCustomer();
    Optional<Customer> getCustomerById(Integer customerId);


}
