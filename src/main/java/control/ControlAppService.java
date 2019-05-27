package control;

import java.util.Scanner;


import control.enums.Option;
import model.Customer;
import service.CustomerServiceImpl;


public class ControlAppService {

    static Scanner scanner = new Scanner(System.in);
    DataManager dataManager = new DataManager();
    CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance();


    public ControlAppService() {

    }

    public void controlLoop() {
        printMenu();
        Option option = null;
        while (true) {

            switch (option) {

                case EXIT: {
                    exit();
                    break;
                }
                case ADD_CUSTOMER: {
                    addCustomer(creatCustomer());
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

    private void printMenu() {

    }

    private Customer creatCustomer() {

        String name = dataManager.getLine(" GIVE A NAME ");
        String surname = dataManager.getLine(" GIVE SURNAME ");
        Integer age = dataManager.getInt("GIVE AGE");
        String email = dataManager.getLine("GIVE EMAIL");

        return new Customer().builder().id(null).name(name).surname(surname).age(age).email(email).build();
    }

}
