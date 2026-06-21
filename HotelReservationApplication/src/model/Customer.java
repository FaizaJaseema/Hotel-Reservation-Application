package model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^(.+)@(.+)\\.(.+)$");

    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(String firstName, String lastName, String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(
                    "Invalid email format. Please use: name@domain.com"
            );
        }
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return String.format("Customer: %s %s | Email: %s", firstName, lastName, email);
    }
}