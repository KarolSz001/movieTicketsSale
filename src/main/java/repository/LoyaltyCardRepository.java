package repository;

import exception.AppException;
import model.Loyalty_Card;
import repository.generic.AbstractCrudRepository;

public class LoyaltyCardRepository extends AbstractCrudRepository<Loyalty_Card, Integer> {


    @Override
    public void add(Loyalty_Card item) {
        if(item == null){
            throw new AppException(" add wrong argument - > null");
        }

        connection.withHandle(handle ->
                handle.execute("INSERT INTO loyalty_card (expiration_date, discount, movies_number, current_movies_number) values (?, ?, ?, ?)", item.getExpirationDate(), item.getDiscount(), item.getMoviesNumber(), item.getCurrent_movies_number()));

    }

    @Override
    public void update(Integer integer, Loyalty_Card item) {

    }
    public void createLoyaltycard(Integer customerId){

    }


}
