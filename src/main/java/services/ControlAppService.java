package services;

import services.dataGenerator.DataManager;
import model.*;
import screen.MenuPrinter;


public class ControlAppService {

    private static ControlAppService instance;
    private final MenuPrinter menuPrinter = MenuPrinter.getInstance();
    private final DataManager dataManager = new DataManager();
    private final CustomerService customerService = new CustomerService();
    private final MovieService movieService = MovieService.getInstance();
    private final SaleTicketService saleTicketService = SaleTicketService.getInstance();


    private boolean loopCustomer;
    private boolean loopMovie;
    private boolean loopTickets;
    private boolean loopOn;

    private ControlAppService() {
    }

    public static ControlAppService getInstance(){
        if(instance == null){
            instance = new ControlAppService();
        }
        return instance;
    }

    public void controlLoop() {
        loopOn = true;
        while (loopOn) {

            setAllLoops(true);
            menuPrinter.startMenu();
            Integer read = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

            switch (read) {
                case 0: {
                    menuPrinter.printExit();
                    loopOn = false;
                    return;
                }
                case 1: {
                    while (loopCustomer) {
                        menuPrinter.printCustomersMenu();
                        System.out.println();
                        Integer readCustomer = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

                        switch (readCustomer) {

                            case 0: {
                                setAllLoops(false);
                                break;
                            }
                            case 1: {
                                customerService.printAllCustomers();
                                break;
                            }
                            case 2: {
                                Integer customerId = dataManager.getInt(" GIVE CUSTOMER ID");
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
                case 2: {
                    while (loopMovie) {
                        menuPrinter.printMoviesMenu();
                        System.out.println();
                        Integer choice = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

                        switch (choice) {
                            case 0: {
                                setAllLoops(false);
                                break;
                            }
                            case 1: {
                                movieService.showAllMoviesToday();
                                break;
                            }
                            case 2: {
                                Integer id = dataManager.getInt(" PRESS ID MOVIE NUMBER ");
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
                case 3:
                    while (loopTickets) {
                        menuPrinter.printSaleTicketServiceMenu();
                        System.out.println();
                        Customer customer = customerService.getCustomerOperation();
                        Integer choice = dataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

                        switch (choice) {
                            case 0: {
                                setAllLoops(false);
                                break;
                            }
                            case 1: {
                                System.out.println(" WELCOME TO SERVICE TICKETS APPLICATION ");
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
        }
    }

    private void setAllLoops(boolean flag) {
        loopCustomer = flag;
        loopMovie = flag;
        loopTickets = flag;
    }


}
