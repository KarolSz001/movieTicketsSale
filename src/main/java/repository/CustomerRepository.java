package repository;

import model.Customer;
import repository.generic.AbstractCrudRepository;

public class CustomerRepository extends AbstractCrudRepository<Customer, Integer> {


    @Override
    public void add(Customer item) {
        connection.withHandle(handle ->
                handle.execute("INSERT INTO customer (name, surname, age, email, loyalty_card_id) values (?, ?, ?, ?, ?)", item.getName(), item.getSurname(), item.getAge(), item.getEmail(), item.getLoyalty_card_id()));
    }
    @Override
    public void update(Integer integer, Customer item) {
        connection.withHandle(handle ->
                handle
                        .createUpdate("UPDATE customer set name = :name, surname = :surname, age = :age, email = :email, loyalty_card_id = :loyalty_card_id WHERE id = :id ")
                        .bind("name", integer)
                        .bind("name", item.getName())
                        .bind("surname", item.getSurname())
                        .bind("age", item.getAge())
                        .bind("email", item.getEmail())
                        .bind("loyalty_card-id", item.getLoyalty_card_id())
                        .execute()
        );
    }


}
