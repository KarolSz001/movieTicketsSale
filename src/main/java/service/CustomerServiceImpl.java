package service;

import api.CustomerService;
import model.Customer;
import repository.CustomerRepository;
import repository.LoyaltyCardRepository;
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
    private LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
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
    public Optional<Customer> getCustomerById(Integer customerId) {
        return customerRepository.findOne(customerId);
    }


    public Optional<Customer> getCustomerByEmail(String customerEmail) {
        return getAllCustomer().stream().filter(f -> f.getEmail().equals(customerEmail)).findFirst();
    }

    private boolean isEmailAlreadyExist(String email) {
        Optional<Customer> customer = getCustomerByEmail(email);
        return customer.isPresent();
    }

    public Integer getCountCustomers() {
        return getAllCustomer().size();
    }

    public List<Customer> getAllCustomersWithLoyaltyCard() {
        return getAllCustomer().stream().filter(f -> f.getLoyalty_card_id() != null).collect(Collectors.toList());
    }

    public void updateCustomer(Customer customer) {
        Integer id = customer.getId();
        customerRepository.update(id, customer);
    }

    public boolean isCardAvailable(Integer customerId) {
        if (!hasLoyalCard(customerId)) {
            long result = salesStandRepository.findAll().stream().filter(f -> f.getCustomerId().equals(customerId)).count();
            return result >= DISCOUNT_LIMIT;
        }
        return false;
    }

    public boolean hasLoyalCard(Integer customerId) {
        return customerRepository.findOne(customerId).get().getLoyalty_card_id() != 0;
    }

    public void addIdLoyalCardToCustomer(Integer idCard, Integer customerId) {
        customerRepository.addIdLoyaltyCardToCustomer(idCard, customerId);
    }

    public boolean isCardActive(Integer customerId) {
        Integer idCard = customerRepository.findOne(customerId).get().getLoyalty_card_id();
        return loyaltyCardRepository.findOne(idCard).get().getCurrent_movies_number() < loyaltyCardRepository.findOne(idCard).get().getMoviesNumber();
    }

}


