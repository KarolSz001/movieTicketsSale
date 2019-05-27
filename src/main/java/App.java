import model.Customer;
import repository.CustomerRepository;
import repository.MovieReposiory;
import repository.connect.DbConnect;
import control.ControlAppService;
import dataGenerator.DataGenerator;

public class App {

    public static void main(String[] args) {

        final String appName = "movieTicketSale v1.0 25.05.2019";
        System.out.println(appName);
        ControlAppService controlAppService = new ControlAppService();
        controlAppService.controlLoop();

        DataGenerator dataGenerator = new DataGenerator();

        DbConnect dbConnect = DbConnect.getInstance();


        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = new Customer(null,"ala","orlala",33,"oralla@o2.pl",null);
        customerRepository.add(customer);
        customerRepository.findAll().forEach(System.out::println);

        MovieReposiory movieReposiory = new MovieReposiory();
//        movieReposiory.findOne(10).ifPresent(System.out::println);
//        movieReposiory.findAll().forEach(System.out::println);
        movieReposiory.getAll().forEach(System.out::println);



    }
}
