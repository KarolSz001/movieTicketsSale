import repository.CustomerRepository;
import repository.LoyaltyCardRepository;
import repository.MovieRepository;
import repository.SalesStandRepository;
import services.ControlAppService;
import services.CustomerService;
import services.MovieService;
import services.SaleTicketService;
import services.dataGenerator.DataGenerator;
import valid.CustomerValidator;

import java.time.LocalDate;


public class App {

    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        sb.append(" ----------------------------------------------------------------------------- \n");
        sb.append(" movieTicketSale v1.0 25.06.2019 \n ");
        sb.append(" Karol Szot \n");
        sb.append(" ----------------------------------------------------------------------------- \n");
        System.out.println(sb.toString());


        var movieRepository = new MovieRepository();
        var customerRepository = new CustomerRepository();
        var customerValidator = new CustomerValidator();
        var salesStandRepository = new SalesStandRepository();
        var loyaltyCardRepository = new LoyaltyCardRepository();

        var customerService = new CustomerService(
                movieRepository, customerRepository, customerValidator, salesStandRepository, loyaltyCardRepository);
        var movieService = new MovieService();
        var saleTicketService = new SaleTicketService();

        var controlAppService = new ControlAppService(customerService,movieService,saleTicketService);


//        System.out.println(LocalDate.now());
        var dataGenerator = new DataGenerator();
        controlAppService.controlLoop();
    }
}
