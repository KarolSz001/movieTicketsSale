package control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import dataGenerator.DateGenerator;
import model.*;
import repository.LoyaltyCardRepository;
import repository.SalesStandRepository;
import service.CustomerServiceImpl;
import service.MovieServiceImpl;


public class ControlAppService {


    private final DataManager dataManager = new DataManager();
    private final DateGenerator dateGenerator = new DateGenerator();
    private final CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance();
    private final MovieServiceImpl movieServiceImpl = MovieServiceImpl.getInstance();
    private final SalesStandRepository salesStandRepository = new SalesStandRepository();
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final LocalTime HIGH_RANGE_TIME = LocalTime.of(22, 30);
    private final Integer DISCOUNT_LIMIT = 2;
    private final Double DISCOUNT_VALUE = 8.0;

    private boolean loopCustomer;
    private boolean loopMovie;
    private boolean loopTickets;
    private boolean loopOn = true;

    public ControlAppService() {

    }

    public void controlLoop() {

        while (loopOn) {
            setAllLoopsON();
            startMenu();
            Integer read = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");
            switch (read) {

                case 0: {
                    exit();
                    loopOn = false;
                    return;
                }
                case 1: {

                    while (loopCustomer) {
                        printCustomersMenu();
                        System.out.println();
                        Integer readCustomer = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

                        switch (readCustomer) {

                            case 0: {
                                setAllLoopsOff();
                                break;
                            }
                            case 1: {
                                printAllCustomers();
                                break;
                            }
                            case 2: {
                                getCustomerById();
                                break;
                            }
                            case 3: {
                                removeCustomerById();
                                break;
                            }
                            case 4: {
                                Customer customer = creatCustomer();
                                addCustomer(customer);
                                break;
                            }
                            case 5: {
                                editCustomerById();
                            }
                            case 6: {
                                customerGeneratorDate();
                            }
                        }
                    }
                }
                case 2: {

                    while (loopMovie) {
                        printMoviesMenu();
                        System.out.println();
                        Integer choice = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

                        switch (choice) {
                            case 0: {
                                setAllLoopsOff();
                                break;
                            }
                            case 1: {
                                showAllMoviesToday();
                                break;
                            }
                            case 2: {
                                Integer id = dataManager.getInt(" PRESS ID MOVIE NUMBER ");
                                showMovieById(id);
                                break;
                            }
                            case 3: {
                                removeMovieById();
                            }
                        }
                    }
                }
                case 3:
                    if (loopTickets) {
                        System.out.println(" WELCOME TO SERVICE TICKETS APPLICATION ");
                        saleTicketOperation();
                        setAllLoopsOff();
                    }

            }
        }
    }

    private void setAllLoopsON() {

        loopCustomer = true;
        loopMovie = true;
        loopTickets = true;
    }

    private void setAllLoopsOff() {

        loopCustomer = false;
        loopMovie = false;
        loopTickets = false;
    }

    private LocalTime getScreeningHours() {

        System.out.println(" SCREENING HOURS ...... ");
        printAvailableTime();
        Integer hh = dataManager.getInt(" give a hour ");
        Integer mm = dataManager.getInt(" give minutes ");
        return correctTime(LocalTime.of(hh, mm));


    }

    private Integer getMovieById() {

        System.out.println(" BELOW MOVIES WHICH ARE AVAILABLE TODAY ");
        showAllMoviesToday();
        Integer idMovie = dataManager.getInt(" PRESS ID MOVIE NUMBER AS YOU CHOICE ");
        showMovieById(idMovie);
        return idMovie;
    }

    private Customer getCustomerOperation() {

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
            customer = dateGenerator.singleCustomerGenerator();
            System.out.println(" CREATED RANDOM CUSTOMER ---->>>>> " + customer);
            dataManager.getLine(" PRESS KEY TO CONTINUE AND SEE WHAT WE HAVE TODAY TO WATCH ");
            addCustomer(customer);
        }
        return customer;
    }

    boolean hasLoyalatycard(Customer customer) {
        return getCustomerByEmail(customer.getEmail()).get().getLoyalty_card_id() != null;
    }


    private void saleTicketOperation() {

        Sales_Stand sales_stand;
        Customer customer = getCustomerOperation();
        Integer idMovie = getMovieById();
        Integer customerId = getCustomerByEmail(customer.getEmail()).get().getId();
        LocalDate date = LocalDate.now();
        LocalTime time = getScreeningHours();
        LocalDateTime dateTime = LocalDateTime.of(date, time);

// card is available if customer don't have one (no 0) and (watched limit movies and time exceeded)
        if (isCardAvailableForCustomer(customerId)) {
            boolean isConfirmation = dataManager.getBoolean("DO YOU WANNA GET LOYAL CARD ??");
            if (isConfirmation) {
                addLoyalty(customer);
            }
        }

        if (hasDiscount(customerId)) {
            // price is lower by discount and decrease number of watched movies in loyal card
            sales_stand = new Sales_Stand().builder().customerId(customerId).movieId(idMovie).start_date_time(dateTime).discount(true).build();
            addTicketToDataBase(sales_stand);
            MovieWithDateTime movieWithDateTime = sendConfirmationOfSellingTicket(customer.getEmail());
            discountPriceTicket(movieWithDateTime);
            System.out.println(" SEND CONFIRMATION OF SELLING TICKET -----> \n" + movieWithDateTime);
        } else {
            sales_stand = new Sales_Stand().builder().customerId(customerId).movieId(idMovie).start_date_time(dateTime).discount(false).build();
            addTicketToDataBase(sales_stand);
            System.out.println(" SEND CONFIRMATION OF SELLING TICKET -----> \n" + sendConfirmationOfSellingTicket(customer.getEmail()));
        }


    }

    private void discountPriceTicket(MovieWithDateTime item) {
        Double priceAfterDiscount = item.getPrice() - (DISCOUNT_VALUE * item.getPrice());
        item.setPrice(priceAfterDiscount);
    }


    private boolean hasDiscount(Integer customerId) {
        if (!customerServiceImpl.hasLoyalCard(customerId)) {
            return false;
        } else {
            return isLoyalCardActive(customerId);
        }
    }

    private boolean isLoyalCardActive(Integer customerId) {
        return customerServiceImpl.isCardActive(customerId);
    }

    private boolean isCardAvailableForCustomer(Integer customerId) {
        return customerServiceImpl.isCardAvailable(customerId);
    }

    boolean isCartCustomerAvailable(Customer customer) {
        long result = getAllTicketsInfo().stream().filter(f -> f.getEmail().equals(customer.getEmail())).count();
        System.out.println(result);
        return result >= DISCOUNT_LIMIT;
    }

    private void printAllCustomerTickets(String customerEmail) {
        movieServiceImpl.getInfo().stream().filter(f -> f.getEmail().equals(customerEmail)).forEach(System.out::println);
    }

    // filtr last movie in sell history for this customer (byEmail)
    private MovieWithDateTime sendConfirmationOfSellingTicket(String customerEmail) {
        return movieServiceImpl.getInfo().stream().filter(f -> f.getEmail().equals(customerEmail)).max(Comparator.comparing(MovieWithDateTime::getId)).get();
    }

    private List<MovieWithDateTime> sellingTicketInformationList() {
        return movieServiceImpl.getInfo();
    }

    private void addTicketToDataBase(Sales_Stand ss) {
        salesStandRepository.add(ss);
    }


    public void printAllTicketsInformation() {
        getAllTicketsInfo().forEach(System.out::println);
    }

    private List<MovieWithDateTime> getAllTicketsInfo() {
        return movieServiceImpl.getInfo();
    }

    private void addLoyalty(Customer customer) {

        Integer customerId = getCustomerByEmail(customer.getEmail()).get().getId();
        LocalDate date = LocalDate.now().plusMonths(1);
        Double discount = 2.0;
        Integer moviesNumber = 3;
        Integer currentMoviesNumber = 0;
        Loyalty_Card loyaltyCard = new Loyalty_Card().builder().expirationDate(date).discount(discount).moviesNumber(moviesNumber).current_movies_number(currentMoviesNumber).build();
        loyaltyCardRepository.add(loyaltyCard);
        int sizeOfCardList = loyaltyCardRepository.findAll().size();
        Integer idLoyaltyCard = loyaltyCardRepository.findAll().get(sizeOfCardList - 1).getId();
        customerServiceImpl.addIdLoyalCardToCustomer(idLoyaltyCard, customerId);
        System.out.println(" ADDED NEW LOYALTY_CARD FOR CUSTOMER \n");

    }


    public void printAvailableTime() {

        LocalTime counter = correctTime(LocalTime.now());
        System.out.println(" correct time " + counter);

        while (counter.isBefore(HIGH_RANGE_TIME)) {
            System.out.println(counter);
            counter = counter.plusMinutes(30);
        }
        System.out.println(counter);
    }

    public LocalTime correctTime(LocalTime localTime) {
        LocalTime lt = LocalTime.parse(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        if (isItTimeOverRange(lt)) {
            System.out.println(" IT IS INCORRECT TIME, YOU WILL GET LAST POSSIBLE ");
            return HIGH_RANGE_TIME;
        }
        if (lt.getMinute() < 30) {
            return LocalTime.of(lt.getHour(), 30);
        } else {
            if (lt.getHour() == 22) {
                System.out.println(" IT IS TOO LATE ");
                return null;
            } else {
                return LocalTime.of(lt.getHour() + 1, 0);
            }
        }
    }

    private boolean isItTimeOverRange(LocalTime localTime) {
        return localTime.isAfter(HIGH_RANGE_TIME) && localTime.isBefore(LocalTime.now());
    }


    private void showMovieById(Integer id) {
        System.out.println(movieServiceImpl.getMovieById(id));
    }

    private void removeMovieById() {
        Integer id = dataManager.getInt(" PRESS ID MOVIE NUMBER");
        movieServiceImpl.removeMovieById(id);
    }


    private void showAllMoviesToday() {

        if (isMoviesBaseEmpty()) {
            System.out.println(" DATABASE IS EMPTY \n");
        } else {
//            getAllMovies().stream().filter(f -> f.getDate().equals(LocalDate.now())).forEach(System.out::println);
            getAllMovies().forEach(System.out::println);
//            movieServiceImpl.printAllData().forEach(System.out::println);
        }
    }

    private List<Movie> getAllMovies() {
        return movieServiceImpl.getAll();
    }


    boolean isMoviesBaseEmpty() {
        return movieServiceImpl.getAllMovies().isEmpty();
    }

    boolean isCustomerBaseEmpty() {
        return customerServiceImpl.getAllCustomer().isEmpty();
    }

    private void printAllCustomers() {
        if (isCustomerBaseEmpty()) {
            System.out.println(" DATABASE IS EMPTY \n");
        } else {
            getAllCustomers().forEach(System.out::println);
        }
    }

    private List<Customer> getAllCustomers() {
        return customerServiceImpl.getAllCustomer();
    }

    private Optional<Customer> getCustomerByEmail(String email) {
        return getAllCustomers().stream().filter(f -> f.getEmail().equals(email)).findFirst();
    }

    private Optional getCustomerById() {
        Integer id = dataManager.getInt(" PRESS ID CUSTOMER ");
        return customerServiceImpl.getCustomerById(id);
    }

    private void customerGeneratorDate() {
        List<Customer> customerList = dateGenerator.customersGenerator();
        for (Customer customer : customerList) {
            System.out.println(customer);
            addCustomer(customer);
        }
    }

    private void editCustomerById() {
        Integer id = dataManager.getInt(" PRESS ID CUSTOMER TO EDIT ");
        Customer customer = new Customer();
        customer.setId(id);
        customerServiceImpl.updateCustomer(customer);
    }

    private void removeCustomerById() {
        Integer id = dataManager.getInt(" PRESS ID CUSTOMER ");
        customerServiceImpl.removeCustomerById(id);
    }

    private void exit() {
        System.out.println(" EXIT APPLICATION - GOODBYE ");
    }

    private void addCustomer(Customer customer) {
        customerServiceImpl.addCustomer(customer);
    }

    private void startMenu() {

        System.out.println(" MAIN LOOP MENU ");
        System.out.println(" 0 - EXIT PROGRAM ");
        System.out.println(" 1 - CUSTOMERS MENU ");
        System.out.println(" 2 - MOVIES MENU ");
        System.out.println(" 3 - SALE TICKETS SERVICE ");

    }

    private void printCustomersMenu() {

        System.out.println(" CUSTOMER MENU ");
        System.out.println(" 0 - COME BACK TO MAIN MENU ");
        System.out.println(" 1 - SHOW ALL CUSTOMERS ");
        System.out.println(" 2 - SHOW CUSTOMER BY ID ");
        System.out.println(" 3 - REMOVE CUSTOMER BY ID ");
        System.out.println(" 4 - CREATE CUSTOMER ");
        System.out.println(" 5 - EDIT CUSTOMER BY ID ");
        System.out.println(" 6 - ADD RANDOM CUSTOMERS ");


    }

    private void printMoviesMenu() {

        System.out.println(" MOVIE MENU ");
        System.out.println(" 0 - COME BACK TO MAIN MENU ");
        System.out.println(" 1 - SHOW ALL MOVIES ");
        System.out.println(" 2 - SHOW MOVIES BY ID ");
        System.out.println(" 3 - REMOVE MOVIES BY ID ");
        System.out.println(" 4 - ADD NEW MOVIE ");
    }

    private Customer creatCustomer() {

        String name = dataManager.getLine(" GIVE A NAME ");
        String surname = dataManager.getLine(" GIVE SURNAME ");
        Integer age = dataManager.getInt(" GIVE AGE ");
        String email = dataManager.getLine(" GIVE EMAIL ");

        return new Customer().builder().id(null).name(name).surname(surname).age(age).email(email).build();
    }

}
