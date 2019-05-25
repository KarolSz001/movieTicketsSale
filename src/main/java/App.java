import connect.DbConnect;
import control.ControlAppService;
import dataGenerator.DataGenerator;
import dataGenerator.MovieStoresJsonConverter;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

public class App {

    public static void main(String[] args) {
        final String appName = "movieTicketSale v1.0 25.05.2019";
        System.out.println(appName);

        ControlAppService controlAppService = new ControlAppService();

        DbConnect dbConnect = DbConnect.getInstance();
        dbConnect.getConnection();

        /*DataGenerator dataGenerator = new DataGenerator();
        List<String> result  =  dataGenerator.readTxtFile();
        result.forEach(System.out::println);*/
        DataGenerator dataGenerator = new DataGenerator();



    }
}
