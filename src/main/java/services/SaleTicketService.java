package services;

import dataGenerator.DataManager;
import model.Customer;
import model.Movie;

import java.util.Map;

public class SaleTicketService {

    private Map<Customer, Movie> ticketsReg;
    private DataManager dataManager = new DataManager();

    public SaleTicketService(Map<Customer, Movie> ticketsReg) {
        this.ticketsReg = ticketsReg;
    }

    public Map<Customer, Movie> getTicketsReg() {
        return ticketsReg;
    }

    public void setTicketsReg(Map<Customer, Movie> ticketsReg) {
        this.ticketsReg = ticketsReg;
    }

    @Override
    public String toString() {
        return "SaleTicketService{" +
                "ticketsReg=" + ticketsReg +
                '}';
    }


    private void initTicketSale(){

    }








}
