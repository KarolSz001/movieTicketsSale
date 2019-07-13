package valid;

import model.Customer;
import valid.generic.AbstractValidator;
import java.util.Map;

public class CustomerValidatorKM extends AbstractValidator<Customer> {
  /*  private final int MIN_LENGTH_NAME = 2;
    private final String NAME_REGEX = "[A-Za-z]+";
    private final String EMAIL_REGEX = "[A-Za-z]+\\.[A-Za-z]+@(gmail.com|onet.pl|wp.pl|interia.pl|o2.pl)";*/

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
