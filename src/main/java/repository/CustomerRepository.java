package repository;

import model.Customer;
import repository.generic.AbstractCrudRepository;

public class CustomerRepository extends AbstractCrudRepository<Customer, Integer> {


    @Override
    public void add(Customer customer) {
        connection.withHandle(handle ->
                handle.execute("INSERT INTO customer (name, surname, age, email, loyalty_card_id) values (?, ?, ?, ?, ?)", customer.getName(), customer.getSurname(), customer.getAge(), customer.getEmail(), customer.getLoyalty_card_id()));
    }

    @Override
    public void update(Integer integer, Customer customer) {
        connection.withHandle(handle ->
                handle
                        .createUpdate("UPDATE customer set name = :name, surname = :surname, age = :age, email = :email, loyalty_card_id = :loyalty_card_id WHERE id = :id ")
                        .bind("name", integer)
                        .bind("name", customer.getName())
                        .bind("surname", customer.getSurname())
                        .bind("age", customer.getAge())
                        .bind("email", customer.getEmail())
                        .bind("loyalty_card-id", customer.getLoyalty_card_id())
                        .execute()
        );
    }


}
