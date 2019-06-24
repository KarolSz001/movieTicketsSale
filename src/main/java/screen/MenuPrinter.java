package screen;

public class MenuPrinter {

    public MenuPrinter() {
    }

    public void startMenu() {

        System.out.println(" MAIN LOOP MENU ");
        System.out.println(" 0 - EXIT PROGRAM ");
        System.out.println(" 1 - CUSTOMERS MENU ");
        System.out.println(" 2 - MOVIES MENU ");
        System.out.println(" 3 - SALE TICKETS SERVICE ");

    }

    public void printCustomersMenu() {

        System.out.println(" CUSTOMER MENU ");
        System.out.println(" 0 - COME BACK TO MAIN MENU ");
        System.out.println(" 1 - SHOW ALL CUSTOMERS ");
        System.out.println(" 2 - SHOW CUSTOMER BY ID ");
        System.out.println(" 3 - REMOVE CUSTOMER BY ID ");
        System.out.println(" 4 - CREATE CUSTOMER ");
        System.out.println(" 5 - EDIT CUSTOMER BY ID ");
        System.out.println(" 6 - ADD RANDOM CUSTOMERS ");
        System.out.println(" 7 - SORT CUSTOMER BY SURNAME ");
        System.out.println(" 8 - SELECT CUSTOMER WITH LOYAL_CARDS");
        System.out.println(" 9 - SORT CUSTOMER BY NUMBER OF WATCHED MOVIES ");


    }

    public void printMoviesMenu() {

        System.out.println(" MOVIE MENU ");
        System.out.println(" 0 - COME BACK TO MAIN MENU ");
        System.out.println(" 1 - SHOW ALL MOVIES ");
        System.out.println(" 2 - SHOW MOVIES BY ID ");
        System.out.println(" 3 - REMOVE MOVIES BY ID ");
        System.out.println(" 4 - ADD NEW MOVIE ");
        System.out.println(" 5 - SORT MOVIES BY DURATION TIME ");
        System.out.println(" 6 - EDIT MOVIES BY ID ");
    }

    public void printExit() {
        System.out.println(" EXIT APPLICATION - GOODBYE ");
    }

}
