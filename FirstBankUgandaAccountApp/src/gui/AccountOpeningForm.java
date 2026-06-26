 package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.util.*;
import java.util.List;

import model.*;
import util.*;

public class AccountOpeningForm extends JFrame {

    private JTextField firstNameField, lastNameField, ninField, emailField, confirmEmailField,
                       phoneField, pinField, confirmPinField, depositField, secondNinField;
    private JComboBox<String> accountTypeCombo, branchCombo, yearCombo, monthCombo, dayCombo;
    private JTextArea summaryArea;

    private Account currentAccount;
    private Map<String, String> branchCodes;

    public AccountOpeningForm() {
        setTitle("First Bank Uganda - New Account Opening");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        branchCodes = Map.of("Kampala", "KLA", "Gulu", "GUL", "Mbarara", "MBA", "Jinja", "JIN", "Mbale", "MBE");

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Add fields
        firstNameField = createField(formPanel, gbc, "First Name:", row++);
        lastNameField = createField(formPanel, gbc, "Last Name:", row++);
        ninField = createField(formPanel, gbc, "NIN:", row++);

        // DOB
        gbc.gridy = row++; gbc.gridx = 0;
        formPanel.add(new JLabel("Date of Birth:"), gbc);
        JPanel dobP = new JPanel();
        yearCombo = new JComboBox<>(); for (int y=1950; y<=2026; y++) yearCombo.addItem(String.valueOf(y));
        monthCombo = new JComboBox<>(new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"});
        dayCombo = new JComboBox<>();
        dobP.add(yearCombo); dobP.add(monthCombo); dobP.add(dayCombo);
        gbc.gridx = 1; formPanel.add(dobP, gbc);

        emailField = createField(formPanel, gbc, "Email:", row++);
        confirmEmailField = createField(formPanel, gbc, "Confirm Email:", row++);
        phoneField = createField(formPanel, gbc, "Phone:", row++);
        pinField = createField(formPanel, gbc, "PIN:", row++);
        confirmPinField = createField(formPanel, gbc, "Confirm PIN:", row++);

        accountTypeCombo = new JComboBox<>(new String[]{"Savings","Current","Fixed Deposit","Student","Joint"});
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1; formPanel.add(accountTypeCombo, gbc); row++;

        branchCombo = new JComboBox<>(new String[]{"Kampala","Gulu","Mbarara","Jinja","Mbale"});
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Branch:"), gbc);
        gbc.gridx = 1; formPanel.add(branchCombo, gbc); row++;

        secondNinField = createField(formPanel, gbc, "Second NIN (Joint):", row++);
        secondNinField.setVisible(false);

        depositField = createField(formPanel, gbc, "Opening Deposit (UGX):", row++);

        JButton submit = new JButton("Submit");
        JButton reset = new JButton("Reset");
        JPanel btnPanel = new JPanel();
        btnPanel.add(submit); btnPanel.add(reset);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        summaryArea = new JTextArea(5, 60);
        summaryArea.setEditable(false);
        gbc.gridy = row++; formPanel.add(new JLabel("Account Summary:"), gbc);
        gbc.gridy = row++; formPanel.add(new JScrollPane(summaryArea), gbc);

        add(formPanel);

        accountTypeCombo.addActionListener(e -> {
            currentAccount = getAccountByType((String)accountTypeCombo.getSelectedItem());
            secondNinField.setVisible("Joint".equals(accountTypeCombo.getSelectedItem()));
        });

        submit.addActionListener(e -> handleSubmit());
        reset.addActionListener(e -> resetForm());

        currentAccount = new SavingsAccount();
        updateDays();
        setVisible(true);
    }

    private JTextField createField(JPanel p, GridBagConstraints gbc, String label, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField tf = new JTextField(25);
        p.add(tf, gbc);
        return tf;
    }

    private void updateDays() {
        int y = Integer.parseInt(yearCombo.getSelectedItem().toString());
        int m = monthCombo.getSelectedIndex() + 1;
        YearMonth ym = YearMonth.of(y, m);
        int days = ym.lengthOfMonth();
        dayCombo.removeAllItems();
        for (int i = 1; i <= days; i++) dayCombo.addItem(String.valueOf(i));
    }

    private Account getAccountByType(String type) {
        return switch (type) {
            case "Savings" -> new SavingsAccount();
            case "Current" -> new CurrentAccount();
            case "Fixed Deposit" -> new FixedDepositAccount();
            case "Student" -> new StudentAccount();
            case "Joint" -> new JointAccount();
            default -> new SavingsAccount();
        };
    }

      private void handleSubmit() {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String nin = ninField.getText().trim().toUpperCase();
            String email = emailField.getText().trim();
            String confirmEmail = confirmEmailField.getText().trim();
            String phone = phoneField.getText().trim();
            String pin = pinField.getText().trim();
            String confirmPin = confirmPinField.getText().trim();
            String depositStr = depositField.getText().trim();
            String accountType = (String) accountTypeCombo.getSelectedItem();
            String branch = (String) branchCombo.getSelectedItem();
            String secondNin = secondNinField.getText().trim().toUpperCase();

            List<String> errors = new ArrayList<>();

            if (firstName.isEmpty() || !firstName.matches("[a-zA-Z\\s]{2,30}")) 
                errors.add("First Name must be 2-30 letters only.");
            
            if (lastName.isEmpty() || !lastName.matches("[a-zA-Z\\s]{2,30}")) 
                errors.add("Last Name must be 2-30 letters only.");
            
            if (nin.length() != 14 || !nin.matches("[A-Z0-9]{14}")) 
                errors.add("NIN must be exactly 14 uppercase alphanumeric characters.");
            
            if (!email.equals(confirmEmail) || !email.contains("@")) 
                errors.add("Emails do not match or invalid format.");
            
            if (!phone.startsWith("+256") || phone.length() != 13) 
                errors.add("Phone must be in format +256XXXXXXXXX (13 characters).");
            
            if (!pin.equals(confirmPin) || !pin.matches("\\d{4,6}")) 
                errors.add("PIN must be 4-6 digits and match confirmation.");
            
            if (pin.matches("^(.)\\1{3,}$")) 
                errors.add("PIN cannot be all identical digits (e.g. 0000).");

            // DOB & Age
            int y = Integer.parseInt(yearCombo.getSelectedItem().toString());
            int m = monthCombo.getSelectedIndex() + 1;
            int d = Integer.parseInt(dayCombo.getSelectedItem().toString());
            LocalDate dob = LocalDate.of(y, m, d);
            int age = Period.between(dob, LocalDate.now()).getYears();
            
            if (age < 18 || age > 75) errors.add("Age must be between 18 and 75.");
            if ("Student".equals(accountType) && (age < 18 || age > 25)) 
                errors.add("Student account requires age 18-25.");

            // Deposit
            double deposit = Double.parseDouble(depositStr);
            if (deposit < currentAccount.getMinimumDeposit()) {
                errors.add("Minimum deposit for " + accountType + " is UGX " + currentAccount.getMinimumDeposit());
            }

            if ("Joint".equals(accountType) && secondNin.length() != 14) 
                errors.add("Joint account requires valid Second NIN.");

            if (!errors.isEmpty()) {
                JOptionPane.showMessageDialog(this, String.join("\n", errors), 
                    "Validation Errors", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate Account Number & Save
            String yearStr = String.valueOf(Year.now().getValue());
            String branchCode = branchCodes.getOrDefault(branch, "KLA");
            int seq = DatabaseManager.getNextSequence(branchCode, yearStr);
            String accountNumber = branchCode + "-" + yearStr + "-" + String.format("%06d", seq);

            boolean saved = DatabaseManager.saveAccount(accountNumber, firstName, lastName, nin, email,
                    phone, pin, dob.toString(), accountType, branch, deposit);

             boolean saved = DatabaseManager.saveAccount(...);

            if (saved) {
                String summary = "ACC: " + accountNumber + " | " + firstName + " " + lastName + 
                        " | " + accountType + " | " + branch + " | DOB " + dob + 
                        " | " + phone + " | Deposit " + deposit + " | " + email;
                summaryArea.setText(summary);

                JOptionPane.showMessageDialog(this, 
                    "✅ Account Created Successfully!\nAccount Number: " + accountNumber, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to save to database.\nCheck console for details.", 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
    }
    private void resetForm() {
        firstNameField.setText(""); lastNameField.setText(""); ninField.setText("");
        emailField.setText(""); confirmEmailField.setText(""); phoneField.setText("");
        pinField.setText(""); confirmPinField.setText(""); depositField.setText("");
        summaryArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccountOpeningForm());
    }
}