import control.ControlAppService;
import model.Customer;
import repository.CustomerRepository;
import repository.connect.DbConnect;
import dataGenerator.DateGenerator;

public class App {

    public static void main(String[] args) {

        final String appName = "movieTicketSale v1.0 02.06.2019";
        System.out.println(appName);
        ControlAppService controlAppService = new ControlAppService();
        controlAppService.controlLoop();



        /*DateGenerator dateGenerator = new DateGenerator();
        DbConnect dbConnect = DbConnect.getInstance();
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = new Customer(null,"xxx","xxx",101,"xxx@o2.pl",null);
        customerRepository.add(customer);
        customerRepository.findAll().forEach(System.out::println);*/

       /* MovieRepository movieRepository = new MovieRepository();
//        movieRepository.findOne(10).ifPresent(System.out::println);
//        movieRepository.findAll().forEach(System.out::println);
        movieRepository.getAll().forEach(System.out::println);*/

//       controlAppService.printAvailableTime();
//        System.out.println(controlAppService.correctTime());



    }
}
