import connect.DbConnect;
import control.ControlAppService;

public class App {

    public static void main(String[] args) {
        final String appName = "movieTicketSale v1.0 25.05.2019";
        System.out.println(appName);

        ControlAppService controlAppService = new ControlAppService();

        DbConnect dbConnect = DbConnect.getInstance();
        dbConnect.getConnection();
    }
}
