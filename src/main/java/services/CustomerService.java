package services;

import exception.AppException;
import lombok.RequiredArgsConstructor;
import model.MovieWithDateTime;
import repository.MovieRepository;
import services.dataGenerator.DataGenerator;
import services.dataGenerator.DataManager;
import model.Customer;
import model.Loyalty_Card;
import repository.CustomerRepository;
import repository.LoyaltyCardRepository;
import repository.SalesStandRepository;
import valid.CustomerValidator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomerService {

    private static final Integer DISCOUNT_LIMIT = 1;
    private static CustomerService instance;

    private final MovieRepository movieRepository = new MovieRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final CustomerValidator customerValidator = CustomerValidator.getInstance();
    private final SalesStandRepository salesStandRepository = new SalesStandRepository();
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final DataManager dataManager = new DataManager();
    private final DataGenerator dataGenerator = new DataGenerator();

    private CustomerService() {
    }

    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }


    public void addCustomer(Customer customer) {
        if (validationCustomerBeforeAdd(customer)) {
            customerRepository.add(customer);
        }
    }

    private boolean validationCustomerBeforeAdd(Customer customer) {
        return customerValidator.isValidate(customer) && (!isEmailAlreadyExist(customer.getEmail()));
    }

    public void removeCustomerById(Integer id) {
        customerRepository.delete(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Integer customerId) {
        return customerRepository.findOne(customerId);
    }

    public Optional<Customer> getCustomerByEmail(String customerEmail) {
        return findAll().stream().filter(f -> f.getEmail().equals(customerEmail)).findFirst();
    }

    private boolean isEmailAlreadyExist(String email) {
        return getCustomerByEmail(email).isPresent();
    }


    public void updateCustomer(Customer customer) {
        customerRepository.update(customer.getId(), customer);
    }

    public boolean isCardAvailable(Integer customerId) {
        if (!hasLoyalCard(customerId)) {
            return salesStandRepository.findAll().stream().filter(f -> f.getCustomerId().equals(customerId)).count() >= DISCOUNT_LIMIT;
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
        if (isCardActiveByNumberOfMovies(idCard) && isCardActiveByDate(idCard)) {
            System.out.println(" CARD IS STILL ACTIVE ");
            return true;
        } else {
            System.out.println(" CARD LOST ACTIVATION ");
            return false;
        }
    }

    private boolean isCardActiveByNumberOfMovies(Integer idCard) {
        return getCardById(idCard).getCurrent_movies_number() < getCardById(idCard).getMoviesNumber();
    }

    private boolean isCardActiveByDate(Integer idCard) {
        return !LocalDate.now().isAfter(getCardById(idCard).getExpirationDate());
    }

    private Loyalty_Card getCardById(Integer id) {
        return loyaltyCardRepository.findOne(id).get();
    }

    public Customer getCustomerOperation() {

        System.out.println(" PLEASE GIVE YOU EMAIL TO CHECK IF YOU ARE IN DATABASE ");
        String email = dataManager.getLine(" GIVE EMAIL ");
        Customer customer;
        System.out.println(" CHECKING DATABASE BY EMAIL CUSTOMER ");

        if (getCustomerByEmail(email).isPresent()) {
            System.out.println(" CUSTOMER AVAILABLE ");
            customer = getCustomerByEmail(email).get();
            System.out.println(customer);

        } else {

            System.out.println(" NO CUSTOMER IN DATABASE , LET'S CREATE ONE ");
            customer = dataGenerator.singleCustomerGenerator();
            if (isCustomerEmailAlreadyInDataBase(customer.getEmail())) {
                throw new AppException(" EMAIL IS ALREADY EXIST IN DATABASE ");

            } else {
                Customer customerTemp = singleCustomerCreator();
                if (!validationCustomerBeforeAdd(customer)) {
                    throw new AppException(" VALIDATION CUSTOMER ERROR ");
                } else {
                    addCustomer(customer);
                }

                System.out.println(" CREATED RANDOM CUSTOMER ---->>>>> " + customerTemp);
                dataManager.getLine(" PRESS KEY TO CONTINUE AND SEE WHAT WE HAVE TODAY TO WATCH ");
                addCustomer(customer);
            }
        }
        return customer;
    }

    private Customer singleCustomerCreator() {
        String name = dataManager.getLine(" GIVE YOU NAME ").toUpperCase();
        String surname = dataManager.getLine(" GIVE YOU SURNAME ").toUpperCase();
        Integer age = dataManager.getInt(" GIVE YOU AGE ");
        String email = dataManager.getLine(" GIVE YOU EMAIL ");
        return Customer.builder().name(name).surname(surname).age(age).email(email).build();

    }

    private boolean isCustomerEmailAlreadyInDataBase(String email) {
        return findAll().stream().anyMatch(f -> f.getEmail().equals(email));
    }


    public void customerGeneratorDate() {
        dataGenerator.customersGenerator().stream().peek(this::addCustomer).forEach(System.out::println);
    }

    public void editCustomerById() {
        Customer customer = getCustomerById(dataManager.getInt(" PRESS ID CUSTOMER ")).get();
        updateCustomer(customer);
    }

    public void removeCustomerById() {
        removeCustomerById(dataManager.getInt(" PRESS ID CUSTOMER "));
    }


    public void printAllCustomers() {
        if (isCustomerBaseEmpty()) {
            System.out.println(" DATABASE IS EMPTY \n");
        } else {
            findAll().forEach(System.out::println);
        }
    }

    public Customer creatCustomer() {
        String name = dataManager.getLine(" GIVE A NAME ");
        String surname = dataManager.getLine(" GIVE SURNAME ");
        Integer age = dataManager.getInt(" GIVE AGE ");
        String email = dataManager.getLine(" GIVE EMAIL ");
        return new Customer().builder().id(null).name(name).surname(surname).age(age).email(email).build();
    }


    boolean isCustomerBaseEmpty() {
        return findAll().isEmpty();
    }

    public Integer getNumbersOfCustomers() {
        return findAll().size();
    }

    public List<Customer> getAllCustomersWithLoyaltyCard() {
        return findAll().stream().filter(f -> f.getLoyalty_card_id() != null).peek(System.out::println).collect(Collectors.toList());
    }

    public void sortCustomerBySurname(){
        findAll().stream().sorted((s1,s2)->s2.getSurname().compareTo(s1.getSurname())).forEach(System.out::println);
    }

    public void printCustomersByNumbersWatchedMovies(){
        movieRepository.getInfo().stream()
                                .collect(Collectors.groupingBy(MovieWithDateTime::getEmail))
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        e->e.getValue().stream().map(MovieWithDateTime::getTitle).count()
                                ))
                                .entrySet()
                                .stream()
                                .sorted(Comparator.comparing(Map.Entry::getValue))
                                .forEach(System.out::println);
    }

}


