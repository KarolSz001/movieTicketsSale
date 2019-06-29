import services.ControlAppService;


public class App {

    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        sb.append(" ----------------------------------------------------------------------------- \n");
        sb.append(" movieTicketSale v1.0 25.06.2019 \n ");
        sb.append(" Karol Szot \n");
        sb.append(" ----------------------------------------------------------------------------- \n");
        System.out.println(sb.toString());
        ControlAppService controlAppService = ControlAppService.getInstance();
        controlAppService.controlLoop();
    }
}
