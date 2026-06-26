package util;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,30}$");
    private static final Pattern NIN_PATTERN = Pattern.compile("^[A-Z0-9]{14}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+256\\d{9}$");
    private static final Pattern PIN_PATTERN = Pattern.compile("^\\d{4,6}$");

    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Name is required.";
        name = name.trim();
        if (!NAME_PATTERN.matcher(name).matches()) {
            return "Name must contain only letters (2-30 characters).";
        }
        return null;
    }

    public static String validateNIN(String nin) {
        if (nin == null || !NIN_PATTERN.matcher(nin.trim()).matches()) {
            return "NIN must be exactly 14 uppercase alphanumeric characters.";
        }
        return null;
    }

    public static String validateEmail(String email, String confirmEmail) {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "Invalid email format.";
        }
        if (!email.trim().equals(confirmEmail.trim())) {
            return "Emails do not match.";
        }
        return null;
    }

    public static String validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone.trim()).matches()) {
            return "Phone must be in format +256XXXXXXXXX (9 digits after +256).";
        }
        return null;
    }

    public static String validatePIN(String pin, String confirmPin) {
        if (pin == null || !PIN_PATTERN.matcher(pin.trim()).matches()) {
            return "PIN must be 4-6 numeric digits.";
        }
        if (!pin.trim().equals(confirmPin.trim())) {
            return "PINs do not match.";
        }
        if (pin.trim().matches("^(.)\\1+$")) {  // All digits same
            return "PIN cannot be all identical digits (e.g., 0000).";
        }
        return null;
    }

    public static int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public static String validateAge(int age, String accountType) {
        if (age < 18 || age > 75) {
            return "Age must be between 18 and 75 years.";
        }
        if ("Student".equals(accountType) && (age < 18 || age > 25)) {
            return "Student account applicants must be 18-25 years old.";
        }
        return null;
    }
}
