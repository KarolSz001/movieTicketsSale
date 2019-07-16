package valid;

import model.Customer;
import valid.generic.AbstractValidator;

import java.util.Map;

public class CustomerValidatorKM extends AbstractValidator<Customer> {
    private final int MIN_LENGTH_NAME = 2;
    private final String NAME_REGEX = "[A-Za-z]+";
    private final String EMAIL_REGEX = "[A-Za-z]+\\.[A-Za-z]+@(gmail.com|onet.pl|wp.pl|interia.pl|o2.pl)";

    @Override
    public Map<String, String> validate(Customer customer) {

        errors.clear();

        if (customer == null) {
            errors.put("customer", "object is null");
        }

       /* if (!isNameValid(customer)) {
            errors.put("name", "is not valid: " + customer.getName());
        }*/
        if (!isNameCorrect(customer.getName())) {
            errors.put("name", " name only work with Letters" + customer.getName());
        }
        if (!isSurnameCorrect(customer.getSurname())) {
            errors.put("name", " surname only work with Letters " + customer.getName());
        }
        if (!isNameLengthCorrect(customer.getName())) {
            errors.put("name", " Name is too short " + customer.getName());
        }
        if (!isSurnameLengthCorrect(customer.getName())) {
            errors.put("name", " surname is too short " + customer.getSurname());
        }
        if (!isAgeCorrect(customer.getAge())) {
            errors.put("name", " wrong age number " + customer.getSurname());
        }
        if (!isEmailCorrect(customer.getEmail())) {
            errors.put("name", " email is wrong " + customer.getEmail());
        }

        return null;
    }


    /*private boolean isNameValid(Customer customer) {
        return true;
    }*/

    private boolean isNameCorrect(String name) {
        return name.matches(NAME_REGEX);
    }

    private boolean isSurnameCorrect(String name) {
        return name.matches(NAME_REGEX);
    }

    private boolean isNameLengthCorrect(String name) {
        return name.length() > MIN_LENGTH_NAME;
    }

    private boolean isSurnameLengthCorrect(String surname) {
        return surname.length() > MIN_LENGTH_NAME;
    }

    private boolean isAgeCorrect(Integer age) {
        return age > 0;
    }

    private boolean isEmailCorrect(String email) {
        return email.matches(EMAIL_REGEX);
    }
}

