package services;

import model.*;
import screen.MenuPrinter;
import services.dataGenerator.DataManager;


public class ControlAppService {

    private final CustomerService customerService;
    private final MovieService movieService;
    private final SaleTicketService saleTicketService;


    private boolean loopOn;


    public ControlAppService(CustomerService customerService, MovieService movieService, SaleTicketService saleTicketService) {
        this.customerService = customerService;
        this.movieService = movieService;
        this.saleTicketService = saleTicketService;
    }


    public void controlLoop() {
        loopOn = true;
        while (loopOn) {

            MenuPrinter.startMenu();
            Integer read = DataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

            switch (read) {
                case 0: {
                    MenuPrinter.printExit();
                    return;
                }
                case 1: {
                    customerOperation();
                    break;
                }
                case 2: {
                    moviesOperation();
                    break;
                }
                case 3: {
                    saleOperation();
                    break;
                }
                case 4: {
                    statOperation();
                    break;
                }
            }
        }
    }

    private void moviesOperation() {
        while (true) {
            MenuPrinter.printMoviesMenu();
            System.out.println();
            Integer choice = DataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

            switch (choice) {
                case 0: {
                    return;
                }
                case 1: {
                    movieService.showAllMoviesToday();
                    break;
                }
                case 2: {
                    Integer id = DataManager.getInt(" PRESS ID MOVIE NUMBER ");
                    movieService.showMovieById(id);
                    break;
                }
                case 3: {
                    movieService.removeMovieById();
                    break;
                }
                case 4: {
                    Movie movie = movieService.createMovie();
                    movieService.addMovie(movie);
                    break;
                }
                case 5: {
                    movieService.sortMoviesByDurationTime();
                    break;
                }

                case 6: {
                    movieService.editMovieById();
                    break;
                }
            }
        }
    }

    private void statOperation() {
        while (true) {
            System.out.println(" WELCOME TO STATIC MENU \n");
            MenuPrinter.printStatisticMenu();
            Integer choice = DataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

            switch (choice) {
                case 0: {
                    return;
                }
                case 1: {
                    movieService.printStatisticByMoviePrice();
                    break;
                }
                case 2: {
                    movieService.printStatisticByDurationTime();
                    break;
                }
                case 3: {
                    movieService.printMapOfGenreAndNumberMovies();
                    break;
                }
            }
        }
    }

    private void saleOperation() {
        while (true) {
            System.out.println(" WELCOME TO SERVICE TICKETS SERVICES ");
            Customer customer = customerService.getCustomerOperation();
            MenuPrinter.printSaleTicketServiceMenu();
            System.out.println();
            Integer choice = DataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

            switch (choice) {
                case 0: {
                    return;
                }
                case 1: {
                    saleTicketService.saleTicketOperation(customer);
                    break;
                }
                case 2: {
                    System.out.println(" SEND ALL SALE TICKETS CUSTOMER HISTORY ");
                    saleTicketService.printAllTicketsHistory(customer);
                    break;
                }
                case 3: {
                    System.out.println(" SEND ALL SALE TICKETS CUSTOMER HISTORY WITH CHOOSE GENRE ");
                    saleTicketService.filterAllTicketsHistoryByGenre(customer);
                    break;
                }
                case 4: {
                    System.out.println(" SEND ALL SALE TICKETS CUSTOMER HISTORY WITH CHOOSE MAX DURATION TIME ");
                    saleTicketService.filterAllTicketsHistoryByMaxDurationTime(customer);
                    break;
                }
            }
        }
    }

    private void customerOperation() {

        while (true) {
            MenuPrinter.printCustomersMenu();
            System.out.println();
            Integer readCustomer = DataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

            switch (readCustomer) {

                case 0: {
                    return;
                }
                case 1: {
                    customerService.printAllCustomers();
                    break;
                }
                case 2: {
                    Integer customerId = DataManager.getInt(" GIVE CUSTOMER ID");
                    customerService.getCustomerById(customerId);
                    break;
                }
                case 3: {
                    customerService.removeCustomerById();
                    break;
                }
                case 4: {
                    Customer customer = customerService.creatCustomer();
                    customerService.addCustomer(customer);
                    break;
                }
                case 5: {
                    customerService.editCustomerById();
                    break;
                }
                case 6: {
                    customerService.customerGeneratorDate();
                    break;
                }
                case 7: {
                    customerService.sortCustomerBySurname();
                    break;
                }
                case 8: {
                    customerService.getAllCustomersWithLoyaltyCard();
                    break;
                }
                case 9: {
                    customerService.printCustomersByNumbersWatchedMovies();
                    break;
                }
            }
        }
    }
}
