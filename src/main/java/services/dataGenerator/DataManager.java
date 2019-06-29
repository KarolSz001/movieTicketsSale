package services.dataGenerator;
import exception.AppException;
import java.util.Scanner;


public class DataManager {

    private final Scanner sc = new Scanner(System.in);

    public Integer getInt(String message) {
        System.out.println(message);

        String line = sc.nextLine();
        if (line == null || !line.matches("\\d+")) {
            throw new AppException(" WRONG DATA ");
        }

        return Integer.parseInt(line);
    }

    public Integer getDouble(String message) {
        System.out.println(message);

        String line = sc.nextLine();
        if (line == null || !line.matches("\\d+\\.\\d{2}")) {
            throw new AppException(" WRONG DATA ");
        }

        return Integer.parseInt(line);
    }


    public String getLine(String message) {
        System.out.println(message);
        return sc.nextLine();
    }


    public boolean getBoolean(String message) {
        System.out.println(message + "[y/n]?");
        return sc.nextLine().toUpperCase().charAt(0) == 'Y';
    }

    public void close() {
        if (sc != null) {
            sc.close();
        }
    }

}
