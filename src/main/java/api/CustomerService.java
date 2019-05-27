package api;

import model.Customer;

import java.util.List;

public interface CustomerService {

    void addCustomer(Customer customer);
    void removeCustomerById(Integer id);

    List<Customer> getAllCustomer();
    Customer getCustomerById(Integer customerId);

}
