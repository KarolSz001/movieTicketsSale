package control;


import control.enums.Option;
import exception.AppException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class DataManager {

    private final Scanner sc = new Scanner(System.in);

    public Integer getInt(String message) {
        System.out.println(message);

        String line = sc.nextLine();
        if (line == null || !line.matches("\\d+")) {
            throw new AppException(" WRONG DATA TRY AGAIN ");
        }

        return Integer.parseInt(line);
    }


    public String getLine(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public Option getChoice() {
        Option[] options = Option.values();
        AtomicInteger counter = new AtomicInteger(1);

        Arrays.stream(options).forEach(criterion -> System.out.println(counter.getAndIncrement() + ". " + criterion));
        System.out.println("Enter choice number:");
        String text = sc.nextLine();

        if (!text.matches("[1-" + options.length + "]")) {
            throw new AppException(" Choice number is not correct ");
        }

        return options[Integer.parseInt(text) - 1];
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
