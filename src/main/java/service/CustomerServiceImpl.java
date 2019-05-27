package service;

import api.CustomerService;
import exception.AppException;
import model.Customer;
import repository.CustomerRepository;
import valid.CustomerValidator;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private static CustomerServiceImpl instance = null;
    private CustomerRepository customerRepository = new CustomerRepository();
    private CustomerValidator customerValidator = CustomerValidator.getInstance();


    private CustomerServiceImpl() {
    }

    public static CustomerServiceImpl getInstance() {
        if (instance == null) {
            instance = new CustomerServiceImpl();
        }
        return instance;
    }

    @Override
    public void addCustomer(Customer customer) {
        if (customerValidator.isValidate(customer)) {
            customerRepository.add(customer);
        }
    }

    @Override
    public void removeCustomerById(Integer id) {
        customerRepository.delete(id);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Integer customerId) {
        return customerRepository.findOne(customerId).orElseThrow(() -> new AppException(" getCustomerId NO RESULT"));
    }



}


