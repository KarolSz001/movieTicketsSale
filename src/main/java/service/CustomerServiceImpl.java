package service;

import api.CustomerService;
import exception.AppException;
import model.Customer;
import repository.CustomerRepository;
import repository.SalesStandRepository;
import valid.CustomerValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {

    private static CustomerServiceImpl instance = null;
    private CustomerRepository customerRepository = new CustomerRepository();
    private CustomerValidator customerValidator = CustomerValidator.getInstance();
    private SalesStandRepository salesStandRepository = new SalesStandRepository();
    private final static Integer DISCOUNT_LIMIT = 4;

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

        if (customerValidator.isValidate(customer) && (!isEmailAlreadyExist(customer.getEmail()))) {
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
    public Optional<Customer>  getCustomerById(Integer customerId) {
        return customerRepository.findOne(customerId);
    }


    public Optional<Customer> getCustomerByEmail(String customerEmail) {
        return getAllCustomer().stream().filter(f -> f.getEmail().equals(customerEmail)).findFirst();
    }

    private boolean isEmailAlreadyExist(String email) {
        Optional<Customer> customer = getCustomerByEmail(email);
        return customer.isPresent();
    }

    public Integer getCountCustomers(){
        return getAllCustomer().size();
    }

    public List<Customer> getAllCustomersWithLoyaltyCard(){
        return getAllCustomer().stream().filter(f->f.getLoyalty_card_id() != null).collect(Collectors.toList());
    }

    public void updateCustomer(Customer customer){
        Integer id  = customer.getId();
        customerRepository.update(id, customer);
    }
    public boolean isDiscountAvailable(Customer customer){
        Integer idCustomer = customer.getId();
        long result = salesStandRepository.findAll().stream().filter(f->f.getCustomerId().equals(idCustomer)).count();
        System.out.println(result);

        return result > DISCOUNT_LIMIT ;
    }

    public void addLoyalCard (Customer customer){}




}


