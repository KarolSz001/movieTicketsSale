package services;
import services.dataGenerator.DataManager;
import model.*;
import screen.MenuPrinter;


public class ControlAppService {

    private final MenuPrinter menuPrinter = new MenuPrinter();
    private final DataManager dataManager = new DataManager();
    private final CustomerService customerService = CustomerService.getInstance();
    private final MovieService movieService = MovieService.getInstance();
    private final SaleTicketService saleTicketService = new SaleTicketService();


    private boolean loopCustomer;
    private boolean loopMovie;
    private boolean loopTickets;
    private boolean loopOn;

    public ControlAppService() {
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
                                customerService.getCustomerById(1);
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
                            }
                            case 6: {
                                customerService.customerGeneratorDate();
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
                            }
                        }
                    }
                }
                case 3:
                    if (loopTickets) {
                        System.out.println(" WELCOME TO SERVICE TICKETS APPLICATION ");
                        saleTicketService.saleTicketOperation();
                        setAllLoops(false);
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