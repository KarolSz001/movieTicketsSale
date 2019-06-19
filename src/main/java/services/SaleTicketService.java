package services;

import services.dataGenerator.DataManager;
import model.*;
import repository.LoyaltyCardRepository;
import repository.SalesStandRepository;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

public class SaleTicketService {

    private final DataManager dataManager = new DataManager();
    private final SalesStandRepository salesStandRepository = new SalesStandRepository();
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final CustomerService customerService = new CustomerService();
    private final MovieService movieService = MovieService.getInstance();
    private static final LocalTime HIGH_RANGE_TIME = LocalTime.of(22, 30);
    private static final Integer MOVIES_LIMIT_NUMBER = 2;
    private static final Double DISCOUNT_VALUE = 8.0;


    public SaleTicketService() {
    }

    public void saleTicketOperation() {

        MovieWithDateTime movieWithDateTime;
        Sales_Stand sales_stand;
        Customer customer = customerService.getCustomerOperation();
        customerService.addCustomer(customer);
        dataManager.getLine(" PRESS KEY TO CONTINUE AND SEE WHAT WE HAVE TODAY TO WATCH ");
        Integer idMovie = getMovieById();
        Integer customerId = customerService.getCustomerByEmail(customer.getEmail()).get().getId();
        LocalDate date = LocalDate.now();
        LocalTime time = getScreeningHours();
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (isCardAvailableForCustomer(customerId)) {
            boolean isConfirmation = dataManager.getBoolean(" DO YOU WANNA GET LOYAL CARD ?? ");
            if (isConfirmation) {
                addLoyalty(customer);
            }
        }

        if (hasDiscount(customerId)) {
            System.out.println(" DISCOUNT IS ACTIVE ");
            sales_stand = new Sales_Stand().builder().customerId(customerId).movieId(idMovie).start_date_time(dateTime).discount(true).build();
            addTicketToDataBase(sales_stand);
            movieWithDateTime = sendConfirmationOfSellingTicket(customer.getEmail());
            discountPriceTicket(movieWithDateTime);
            increaseCurrentNumberMovieInLoyalCard(customerId);

        } else {
            System.out.println(" DISCOUNT IS NOT ACTIVE ");
            sales_stand = new Sales_Stand().builder().customerId(customerId).movieId(idMovie).start_date_time(dateTime).discount(false).build();
            addTicketToDataBase(sales_stand);
            movieWithDateTime = sendConfirmationOfSellingTicket(customer.getEmail());
        }

        System.out.println(" SEND CONFIRMATION OF SELLING TICKET -----> \n" + movieWithDateTime);
    }

    private Integer getMovieById() {

        System.out.println(" BELOW MOVIES WHICH ARE AVAILABLE TODAY ");
        movieService.showAllMoviesToday();
        Integer idMovie = dataManager.getInt(" PRESS ID MOVIE NUMBER AS YOU CHOICE ");
        movieService.showMovieById(idMovie);
        return idMovie;
    }

    private LocalTime getScreeningHours() {

        System.out.println("\n SCREENING HOURS ...... ");
        printAvailableTime();
        Integer hh = dataManager.getInt("\n give a hour ");
        Integer mm = dataManager.getInt(" give minutes ");
        if (isTimeOverRange(LocalTime.of(hh, mm))) {
            System.out.println(" IT IS INCORRECT TIME, YOU WILL GET LAST POSSIBLE SCREEN HOUR ");
            return HIGH_RANGE_TIME;
        }

        return correctTime(LocalTime.of(hh, mm));
    }

    public LocalTime correctTime(LocalTime localTime) {
        LocalTime lt = LocalTime.parse(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        if (lt.getMinute() <= 30 && lt.getMinute() > 0 && lt.getHour() <= 22) {
            return LocalTime.of(lt.getHour(), 30);
        }
        if (lt.getMinute() > 30 && lt.getHour() <= 21) {
            return LocalTime.of(lt.getHour() + 1, 0);
        }
        return LocalTime.of(lt.getHour(), 0);
    }

    private boolean isTimeOverRange(LocalTime localTime) {
        return localTime.isAfter(HIGH_RANGE_TIME) || localTime.isBefore(LocalTime.now());
    }

    private void discountPriceTicket(MovieWithDateTime item) {
        DecimalFormatSymbols otherSymbol = new DecimalFormatSymbols(Locale.getDefault());
        DecimalFormat dc = new DecimalFormat("#.##", otherSymbol);
        Double priceAfterDiscount = Double.valueOf(dc.format(item.getPrice() - (DISCOUNT_VALUE * 0.01 * item.getPrice())));
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

    private void printAvailableTime() {

        LocalTime counter = correctTime(LocalTime.now());
        while (counter.isBefore(HIGH_RANGE_TIME)) {
            System.out.print(counter + " || ");
            counter = counter.plusMinutes(30);
        }
    }

    private void increaseCurrentNumberMovieInLoyalCard(Integer itemId) {

        Integer loyaltyCardId = customerService.getCustomerById(itemId).get().getLoyalty_card_id();
        Loyalty_Card loyaltyCard = loyaltyCardRepository.findOne(loyaltyCardId).get();
        int number = loyaltyCard.getCurrent_movies_number() + 1;
        loyaltyCard.setCurrent_movies_number(number);
        loyaltyCardRepository.update(loyaltyCardId, loyaltyCard);
    }


}
