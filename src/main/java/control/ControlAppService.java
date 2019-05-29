package control;

import java.util.Scanner;

import model.Customer;
import service.CustomerServiceImpl;
import service.MovieServiceImpl;


public class ControlAppService {

    static Scanner scanner = new Scanner(System.in);
    DataManager dataManager = new DataManager();
    CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance();
    MovieServiceImpl movieServiceiImpl = MovieServiceImpl.getInstance();
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
                                if (customerServiceImpl.getAllCustomer().isEmpty()) {
                                    System.out.println("DATABASE IS EMPTY \n");
                                }else {
                                    customerServiceImpl.getAllCustomer().forEach(System.out::println);
                                }
                                break;
                            }
                            case 2: {
                                Integer id = dataManager.getInt(" PRESS ID CUSTOMER ");
                                customerServiceImpl.getCustomerById(id);
                                break;
                            }
                            case 3: {
                                Integer id = dataManager.getInt(" PRESS ID CUSTOMER ");
                                customerServiceImpl.removeCustomerById(id);
                                break;
                            }
                            case 4: {
                                Customer customer = creatCustomer();
                                addCustomer(customer);
                                break;
                            }
                            case 5: {
                                Integer id = dataManager.getInt(" PRESS ID CUSTOMER TO EDIT ");
                                Customer customer = new Customer();
                                customer.setId(id);
                                customerServiceImpl.updateCustomer(customer);
                            }
                        }
                    }


                }
            }


        }

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
        System.out.println(" 0 - COME BACK TO MAIN MENU");
        System.out.println(" 1 - SHOW ALL CUSTOMERS");
        System.out.println(" 2 - SHOW CUSTOMER BY ID");
        System.out.println(" 3 - REMOVE CUSTOMER BY ID");
        System.out.println(" 4 - ADD NEW CUSTOMER");
        System.out.println(" 5 - EDIT CUSTOMER BY ID");
    }

    private void printMoviesMenu() {

        System.out.println(" MOVIE MENU ");
        System.out.println(" 0 - COME BACK TO MAIN MENU");
        System.out.println(" 1 - SHOW ALL MOVIES");
        System.out.println(" 2 - SHOW MOVIES BY ID");
        System.out.println(" 3 - REMOVE MOVIES BY ID");
        System.out.println(" 4 - ADD NEW MOVIE ");
        System.out.println(" 5 - EDIT MOVIE BY ID");
    }


    private Customer creatCustomer() {

        String name = dataManager.getLine(" GIVE A NAME ");
        String surname = dataManager.getLine(" GIVE SURNAME ");
        Integer age = dataManager.getInt(" GIVE AGE ");
        String email = dataManager.getLine(" GIVE EMAIL ");

        return new Customer().builder().id(null).name(name).surname(surname).age(age).email(email).build();
    }

}
