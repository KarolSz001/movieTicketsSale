import control.ControlAppService;

public class App {

    public static void main(String[] args) {
        final String appName = "App01 v1.03 27.04.2019 _K.Szot";
        System.out.println(appName);

        ControlAppService controlAppService = new ControlAppService();
        controlAppService.runApp();
    }
}
