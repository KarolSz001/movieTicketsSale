package repository;

import exception.AppException;
import model.Sales_Stand;
import repository.generic.AbstractCrudRepository;

public class SalesStandRepository extends AbstractCrudRepository<Sales_Stand,Integer> {


    @Override
    public void add(Sales_Stand item) {


        if(item == null){
            throw new AppException(" add wrong argument - > null");
        }

        connection.withHandle(handle ->
                handle.execute("INSERT INTO sales_stand (customer_id, movie_id, start_date_time) values (?, ?, ?)", item.getCustomerId(), item.getMovieId(), item.getStart_date_time()));

    }

    @Override
    public void update(Integer integer, Sales_Stand item) {

    }







}
