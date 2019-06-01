package control;

import java.nio.channels.DatagramChannel;
import java.util.List;
import java.util.Scanner;

import dataGenerator.DateGenerator;
import model.Customer;
import service.CustomerServiceImpl;
import service.MovieServiceImpl;


public class ControlAppService {

    static Scanner scanner = new Scanner(System.in);
    private final DataManager dataManager = new DataManager();
    private final DateGenerator dateGenerator = new DateGenerator();
    private final CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance();
    private final MovieServiceImpl movieServiceiImpl = MovieServiceImpl.getInstance();
    boolean loopOn = true;
    boolean loopCustomer = false;
    boolean loopMovie = false;

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
                                if (isDataBaseEmpty()) {
                                    System.out.println(" DATABASE IS EMPTY \n");

                                } else {
                                    getAllCustomers();
                                }
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
                                customerGeneratData();
                            }
                        }
                    }
                }
                case 2: {
                    loopMovie = true;

                    while (loopMovie) {


                    }
                }
            }
        }

    }

    boolean isDataBaseEmpty() {
        return customerServiceImpl.getAllCustomer().isEmpty();
    }

    private void getAllCustomers() {
        customerServiceImpl.getAllCustomer().forEach(System.out::println);
    }

    private void getCustomerById() {
        Integer id = dataManager.getInt(" PRESS ID CUSTOMER ");
        customerServiceImpl.getCustomerById(id);
    }

    private void customerGeneratData() {
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
    }

    private void printCustomersMenu() {

        System.out.println(" CUSTOMER MENU ");
        System.out.println(" 0 - COME BACK TO MAIN MENU ");
        System.out.println(" 1 - SHOW ALL CUSTOMERS ");
        System.out.println(" 2 - SHOW CUSTOMER BY ID ");
        System.out.println(" 3 - REMOVE CUSTOMER BY ID ");
        System.out.println(" 4 - ADD NEW CUSTOMER ");
        System.out.println(" 5 - EDIT CUSTOMER BY ID ");
        System.out.println(" 6 - GENERATE RANDOM DATA");
    }

    private void printMoviesMenu() {

        System.out.println(" MOVIE MENU ");
        System.out.println(" 0 - COME BACK TO MAIN MENU ");
        System.out.println(" 1 - SHOW ALL MOVIES ");
        System.out.println(" 2 - SHOW MOVIES BY ID ");
        System.out.println(" 3 - REMOVE MOVIES BY ID ");
        System.out.println(" 4 - ADD NEW MOVIE ");
        System.out.println(" 5 - EDIT MOVIE BY ID ");
    }


    private Customer creatCustomer() {

        String name = dataManager.getLine(" GIVE A NAME ");
        String surname = dataManager.getLine(" GIVE SURNAME ");
        Integer age = dataManager.getInt(" GIVE AGE ");
        String email = dataManager.getLine(" GIVE EMAIL ");

        return new Customer().builder().id(null).name(name).surname(surname).age(age).email(email).build();
    }

}
