import control.ControlAppService;

import java.time.LocalTime;


public class App {

    public static void main(String[] args) {

        final String appName = "movieTicketSale v1.0 06.06.2019";
        System.out.println(appName);
        ControlAppService controlAppService = new ControlAppService();
//        controlAppService.controlLoop();
        System.out.println(controlAppService.correctTime(LocalTime.of(16,40)));

    }
}
