package services;
import model.*;
import screen.MenuPrinter;
import services.dataGenerator.DataManager;


public class ControlAppService {

    private OperationService operationService;

    public ControlAppService() {

    }

    public void controlLoop() {
         boolean loopOn = true;
        while (loopOn) {

            MenuPrinter.startMenu();
            Integer read = DataManager.getInt(" PRESS NUMBER TO MAKE A CHOICE ");

            switch (read) {
                case 0: {
                    MenuPrinter.printExit();
                    return;
                }
                case 1: {
                    operationService.customerOperation();
                    break;
                }
                case 2: {
                    operationService.moviesOperation();
                    break;
                }
                case 3: {
                    operationService.saleOperation();
                    break;
                }
                case 4: {
                    operationService.statOperation();
                    break;
                }
            }
        }
    }


}
