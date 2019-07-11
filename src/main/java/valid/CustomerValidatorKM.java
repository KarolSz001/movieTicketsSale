package valid;

import model.Customer;
import valid.generic.AbstractValidator;

import java.util.Map;

public class CustomerValidatorKM extends AbstractValidator<Customer> {

    @Override
    public Map<String, String> validate(Customer customer) {

        errors.clear();

        if ( customer == null ) {
            errors.put("customer", "object is null");
        }

        if ( !isNameValid(customer) ) {
            errors.put("name", "is not valid: " + customer.getName());
        }

        return null;
    }

    private boolean isNameValid(Customer customer) {
        return true;
    }
}
