package control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dataGenerator.DateGenerator;
import model.Customer;
import model.Movie;
import model.Sales_Stand;
import repository.SalesStandRepository;
import service.CustomerServiceImpl;
import service.MovieServiceImpl;


public class ControlAppService {

    private final DataManager dataManager = new DataManager();
    private final DateGenerator dateGenerator = new DateGenerator();
    private final CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance();
    private final MovieServiceImpl movieServiceImpl = MovieServiceImpl.getInstance();
    private final Map<Customer, Movie> saleTicketsRegister = new HashMap<>();
    private boolean loopOn = true;
    private boolean loopCustomer = false;
    private boolean loopMovie = false;
    private boolean loopTickets = false;

    public ControlAppService() {

    }


    public void controlLoop() {

        while (loopOn) {
            startMenu();
            Integer read = dataManager.getInt(" PRESS NUMBER FOR YOU CHOICE ");
            switch (read) {

                case 0: {
                    exit();
                    loopOn = false;
                    return;
                }
                case 1: {

                    loopCustomer = true;
                    while (loopCustomer) {
                        printCustomersMenu();
                        System.out.println();
                        Integer readCustomer = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

                        switch (readCustomer) {

                            case 0: {
                                loopCustomer = false;
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

                    loopMovie = true;
                    while (loopMovie) {
                        printMoviesMenu();
                        System.out.println();
                        Integer choice = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

                        switch (choice) {
                            case 0: {
                                loopMovie = false;
                                break;
                            }
                            case 1: {
                                showAllMovies();
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
                    System.out.println(" WELCOME TO SERVICE TICKETS APPLICATION");
                    /*loopTickets = true;
                    System.out.println(" PLEASE GIVE YOU EMAIL TO CHECK IF YOU ARE IN DATABASE ");
                    String email = dataManager.getLine(" GIVE EMAIL ");
                    Customer customer;
                    System.out.println("CHECKING DATABASE BY EMAIL CUSTOMER");
                    if (getCustomerByEmail(email).isPresent()) {
                        System.out.println(" CUSTOMER AVAILABLE ");
                        customer = getCustomerByEmail(email).get();
                        System.out.println(customer);
                    } else {
                        System.out.println(" NO CUSTOMER IN DATABASE , LET's CREATE ONE");
                        customer = creatCustomer();
                        addCustomer(customer);
                    }

                    System.out.println(" BELOW MOVIES WHICH ARE AVAILABLE TODAY ");
                    showAllMovies();
                    System.out.println(" you choice is - > ");
                    System.out.println(" which movie you pick up ");
                    Integer idMovie = dataManager.getInt(" PRESS ID MOVIE NUMBER ");
                    showMovieById(idMovie);
                    System.out.println(" Available time to watch movie ");
                    printAvailableTime();
                    Integer idCustomer = getCustomerByEmail(customer.getEmail()).get().getId();*/

//                    Customer customer = dateGenerator.singleCustomerGenerator();
                    Customer customer = new Customer().builder().name("KASIA").surname("HOHOH").age(101).email("kasia.HOHO@o2.pl").build();
                    addCustomer(customer);

                    Integer movie_id = movieServiceImpl.getMovieById(1).getId();
                    String email = customer.getEmail();
                    customer = customerServiceImpl.getCustomerByEmail(email).get();
                    System.out.println(customer);
                    saleTicketOperation(customer, movie_id);
                    sendConfirmationOfSellingTicket();


            }
        }
    }


    private void saleTicketOperation(Customer customer, Integer movieId) {

        Integer customer_id = customer.getId();
        SalesStandRepository salesStandRepository = new SalesStandRepository();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime dateTime = LocalDateTime.of(date,time);
        Boolean discount = customerServiceImpl.isDiscountAvailable(customer);
        Sales_Stand sales_stand = new Sales_Stand().builder().customerId(customer_id).movieId(movieId).startDateTime(dateTime).discount(discount).build();
        salesStandRepository.add(sales_stand);

    }
    public void sendConfirmationOfSellingTicket(){
// movie info, start time, price of ticket, -> join movie and sale_stand
        movieServiceImpl.getInfo().forEach(System.out::println);
    }

    public void printAvailableTime() {

        LocalTime highRangeTime = LocalTime.of(22, 30);
        LocalTime counter = correctTime();
        System.out.println(" correct time " + counter);
        while (counter.isBefore(highRangeTime)) {
            System.out.println(counter);
            counter = counter.plusMinutes(30);
        }
        System.out.println(counter);
    }

    public LocalTime correctTime() {
        int mins = LocalTime.now().getMinute();
        if (mins < 30) {
            int hour = LocalTime.now().getHour();
            return LocalTime.of(hour, 30);
        } else {
            if (LocalTime.now().getHour() == 22) {
                System.out.println(" IT IS TOO LATE ");
            } else {
                int hour = LocalTime.now().plusHours(1).getHour();
                return LocalTime.of(hour, 0);
            }
        }
        return null;
    }


    private void showMovieById(Integer id) {
        movieServiceImpl.getMovieById(id);
    }

    private void removeMovieById() {
        Integer id = dataManager.getInt(" PRESS ID MOVIE NUMBER");
        movieServiceImpl.removeMovieById(id);
    }


    private void showAllMovies() {

        if (isMoviesBaseEmpty()) {
            System.out.println(" DATABASE IS EMPTY \n");
        } else {
            getAllMovies().forEach(System.out::println);
        }
    }

    private List<Movie> getAllMovies() {
        return movieServiceImpl.getAllMovies();
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
        System.out.println(" 1 - SHOW ALL MOVIES ");
        System.out.println(" 2 - SHOW  MOVIE BY ID ");
        System.out.println(" 3 - REMOVE MOVIE BY ID ");

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
