package control;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dataGenerator.DateGenerator;
import model.Customer;
import model.Movie;
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
                                showMovieById();
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
                    loopTickets = true;
                    System.out.println("PLEASE GIVE YOU EMAIL TO CHECK IF YOU ARE IN DATABASE");
                    String email = dataManager.getLine(" GIVE EMAIL ");

                    System.out.println("CHECKING DATABASE BY EMAIL CUSTOMER");
                    if (getCustomerByEmail(email).isPresent()) {
                        System.out.println(" CUSTOMER AVAILABLE ");
                        Customer customer = getCustomerByEmail(email).get();
                        System.out.println(customer);
                    } else {
                        System.out.println(" NO CUSTOMER IN DATABASE , PRESS Y IF WANNA ADD");
                    }
                    Customer customer = creatCustomer();
                    addCustomer(customer);

                    System.out.println(" BELOW MOVIES WHICH ARE AVAILABLE TODAY AFTER " + LocalTime.now());

            }
        }
    }

    public void printAvailableTime() {

        LocalTime highRangeTime = LocalTime.of(22, 30);
        LocalTime counter = correctTime();
        System.out.println(" correct time " + counter);
        while (counter.isBefore(highRangeTime)){
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


    private void showMovieById() {
        Integer id = dataManager.getInt(" PRESS ID MOVIE ");
        movieServiceImpl.getMovieById(id);
    }

    private void removeMovieById() {
        Integer id = dataManager.getInt(" PRESS ID MOVIE ");
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
        System.out.println(("3 - SALE TICKETS SERVICE"));
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
