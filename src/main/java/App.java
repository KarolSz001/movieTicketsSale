import control.ControlAppService;
import model.Customer;
import repository.CustomerRepository;
import repository.connect.DbConnect;
import dataGenerator.DateGenerator;

public class App {

    public static void main(String[] args) {

        final String appName = "movieTicketSale v1.0 06.06.2019";
        System.out.println(appName);
        ControlAppService controlAppService = new ControlAppService();
        controlAppService.controlLoop();
    }
}
