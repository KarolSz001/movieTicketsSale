import services.ControlAppService;


public class App {

    public static void main(String[] args) {

        final String appName = " movieTicketSale v1.0 18.06.2019 ";
        System.out.println(appName);
        ControlAppService controlAppService = new ControlAppService();
        controlAppService.controlLoop();

    }
}
