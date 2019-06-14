package services;

import api.CustomerService;
import model.Customer;
import model.Loyalty_Card;
import repository.CustomerRepository;
import repository.LoyaltyCardRepository;
import repository.SalesStandRepository;
import valid.CustomerValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerService {

    private static CustomerService instance = null;
    private CustomerRepository customerRepository = new CustomerRepository();
    private CustomerValidator customerValidator = CustomerValidator.getInstance();
    private SalesStandRepository salesStandRepository = new SalesStandRepository();
    private LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final static Integer DISCOUNT_LIMIT = 4;

    private CustomerService() {
    }

    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }


    public void addCustomer(Customer customer) {

        if (customerValidator.isValidate(customer) && (!isEmailAlreadyExist(customer.getEmail()))) {
            customerRepository.add(customer);
        }
    }


    public void removeCustomerById(Integer id) {
        customerRepository.delete(id);
    }


    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }


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
        return customerRepository.findOne(customerId).get().getLoyalty_card_id() != null;
    }

    public void addIdLoyalCardToCustomer(Integer idCard, Integer customerId) {
        customerRepository.addIdLoyaltyCardToCustomer(idCard, customerId);
    }

    public boolean isCardActive(Integer customerId) {
        Integer idCard = customerRepository.findOne(customerId).get().getLoyalty_card_id();
        if (isCardActiveByNumberOfMovies(idCard)) {
            System.out.println("CARD IS STILL ACTIVE");
            return true;
        } else {
            System.out.println("CARD LOST ACTIVATION");
            return false;
        }
    }
    private boolean isCardActiveByNumberOfMovies(Integer idCard){
        return getCardById(idCard).getCurrent_movies_number() < getCardById(idCard).getMoviesNumber();
    }
    private boolean isCardActiveByData(Integer idCard){
        return LocalDate.now().isBefore(getCardById(idCard).getExpirationDate()) || LocalDate.now().isEqual(getCardById(idCard).getExpirationDate());
    }

    private Loyalty_Card getCardById(Integer id) {
        return loyaltyCardRepository.findOne(id).get();
    }

}


