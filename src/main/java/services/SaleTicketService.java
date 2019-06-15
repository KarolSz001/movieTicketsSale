package services;

import dataGenerator.DataManager;
import dataGenerator.DataGenerator;
import model.*;
import repository.LoyaltyCardRepository;
import repository.SalesStandRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class SaleTicketService {

    private final DataManager dataManager = new DataManager();
    private final DataGenerator dataGenerator = new DataGenerator();

    private final SalesStandRepository salesStandRepository = new SalesStandRepository();
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final CustomerService customerService = CustomerService.getInstance();
    private final MovieService movieService = MovieService.getInstance();


    private static final LocalTime HIGH_RANGE_TIME = LocalTime.of(22, 30);
    private static final Integer MOVIES_LIMIT_NUMBER = 2;
    private static final Double DISCOUNT_VALUE = 8.0;


    public SaleTicketService() {
    }

    public void saleTicketOperation() {

        Sales_Stand sales_stand;
        Customer customer = customerService.getCustomerOperation();
        Integer idMovie = getMovieById();
        Integer customerId = customerService.getCustomerByEmail(customer.getEmail()).get().getId();
        LocalDate date = LocalDate.now();
        LocalTime time = getScreeningHours();
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (isCardAvailableForCustomer(customerId)) {
            boolean isConfirmation = dataManager.getBoolean("bDO YOU WANNA GET LOYAL CARD ?? ");
            if (isConfirmation) {
                addLoyalty(customer);
            }
        }

        if (hasDiscount(customerId)) {
            // price is lower by discount and decrease number of watched movies in loyal card
            sales_stand = new Sales_Stand().builder().customerId(customerId).movieId(idMovie).start_date_time(dateTime).discount(true).build();
            addTicketToDataBase(sales_stand);
            MovieWithDateTime movieWithDateTime = sendConfirmationOfSellingTicket(customer.getEmail());
            discountPriceTicket(movieWithDateTime);
            System.out.println(" SEND CONFIRMATION OF SELLING TICKET -----> \n" + movieWithDateTime);

        } else {
            sales_stand = new Sales_Stand().builder().customerId(customerId).movieId(idMovie).start_date_time(dateTime).discount(false).build();
            addTicketToDataBase(sales_stand);
            System.out.println(" SEND CONFIRMATION OF SELLING TICKET -----> \n" + sendConfirmationOfSellingTicket(customer.getEmail()));
        }

    }

    private Integer getMovieById() {

        System.out.println(" BELOW MOVIES WHICH ARE AVAILABLE TODAY ");
        movieService.showAllMoviesToday();
        Integer idMovie = dataManager.getInt(" PRESS ID MOVIE NUMBER AS YOU CHOICE ");
        movieService.showMovieById(idMovie);

        return idMovie;
    }

    private LocalTime getScreeningHours() {

        System.out.println(" SCREENING HOURS ...... ");
        printAvailableTime();
        Integer hh = dataManager.getInt(" give a hour ");
        Integer mm = dataManager.getInt(" give minutes ");

        return correctTime(LocalTime.of(hh, mm));

    }

    public LocalTime correctTime(LocalTime localTime) {
        LocalTime lt = LocalTime.parse(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        if (isTimeOverRange(lt)) {
            System.out.println(" IT IS INCORRECT TIME, YOU WILL GET LAST POSSIBLE SCREEN HOUR ");
            return HIGH_RANGE_TIME;
        }
        if (lt.getMinute() < 30 && lt.getMinute() > 0 && lt.getHour() <= 22) {
            return LocalTime.of(lt.getHour(), 30);
        }
        return LocalTime.of(lt.getHour() + 1, 0);
    }

    private boolean isTimeOverRange(LocalTime localTime) {
        return localTime.isAfter(HIGH_RANGE_TIME) && localTime.isBefore(LocalTime.now());
    }

    private void discountPriceTicket(MovieWithDateTime item) {
        Double priceAfterDiscount = item.getPrice() - (DISCOUNT_VALUE * item.getPrice());
        item.setPrice(priceAfterDiscount);
    }

    private boolean hasDiscount(Integer customerId) {
        if (!customerService.hasLoyalCard(customerId)) {
            return false;
        } else {
            return isLoyalCardActive(customerId);
        }
    }

    private boolean isCardAvailableForCustomer(Integer customerId) {
        return customerService.isCardAvailable(customerId);
    }

    private boolean isLoyalCardActive(Integer customerId) {

        return customerService.isCardActive(customerId);
    }



    private MovieWithDateTime sendConfirmationOfSellingTicket(String customerEmail) {
        return movieService.getInfo().stream().filter(f -> f.getEmail().equals(customerEmail)).max(Comparator.comparing(MovieWithDateTime::getId)).get();
    }

    private void addTicketToDataBase(Sales_Stand ss) {
        salesStandRepository.add(ss);
    }

    private void addLoyalty(Customer customer) {

        Integer customerId = customerService.getCustomerByEmail(customer.getEmail()).get().getId();
        LocalDate date = LocalDate.now().plusMonths(1);
        Loyalty_Card loyaltyCard = new Loyalty_Card().builder().expirationDate(date).discount(DISCOUNT_VALUE).moviesNumber(MOVIES_LIMIT_NUMBER).current_movies_number(0).build();
        loyaltyCardRepository.add(loyaltyCard);
        int sizeOfCardList = loyaltyCardRepository.findAll().size();
        // get last added card
        Integer idLoyaltyCard = loyaltyCardRepository.findAll().get(sizeOfCardList - 1).getId();
        customerService.addIdLoyalCardToCustomer(idLoyaltyCard, customerId);
        System.out.println(" ADDED NEW LOYALTY_CARD FOR CUSTOMER \n");
    }

    public void printAvailableTime() {
        LocalTime counter = correctTime(LocalTime.now());
        while (counter.isBefore(HIGH_RANGE_TIME)) {
            System.out.print(counter + " || ");
            counter = counter.plusMinutes(30);
        }
    }


}
