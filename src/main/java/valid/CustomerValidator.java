package valid;

import exception.AppException;
import model.Customer;

public class CustomerValidator {

    private final int MIN_LENGTH_NAME = 2;
    private final String NAME_REGEX = "[A-Za-z]+";
    private final String EMAIL_REGEX = "[A-Za-z]+\\.[A-Za-z]+@(gmail.com|onet.pl|wp.pl|interia.pl|o2.pl)";

    private static CustomerValidator instance = null;

    private CustomerValidator() {
    }

    public static CustomerValidator getInstance() {
        if (instance == null) {
            instance = new CustomerValidator();
        }
        return instance;
    }

    public boolean isValidate(Customer customer) throws AppException {

        if (!isNameCorrect(customer.getName())) {
            throw new AppException(" Name only work with Letters");
        }

        if (!isSurnameCorrect(customer.getSurname())) {
            throw new AppException("Surname only work with Letters");
        }
        if (!isNameLengthCorrect(customer.getName()))
            throw new AppException(" Name is too short ");

        if (!isSurnameLenghtCorrect(customer.getSurname())) {
            throw new AppException(" Surname is too short ");
        }
        if (!isAgeCorrect(customer.getAge())) {
            throw new AppException(" Age is wrong ");
        }
        if (!isEmailCorrect(customer.getEmail())) {
            throw new AppException(" email is wrong ");
        }
        return true;
    }

    private boolean isNameCorrect(String name) {
        return name.matches(NAME_REGEX);
    }

    private boolean isSurnameCorrect(String name) {
        return name.matches(NAME_REGEX);
    }

    private boolean isNameLengthCorrect(String name) {
        return name.length() > MIN_LENGTH_NAME;
    }

    private boolean isSurnameLenghtCorrect(String surname) {
        return surname.length() > MIN_LENGTH_NAME;
    }

    private boolean isAgeCorrect(Integer age){
        return age > 0;
    }

    private boolean isEmailCorrect(String email){
        return email.matches(EMAIL_REGEX);
    }


    }
