package revature.tariqkhan.dao;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import revature.tariqkhan.consoleutil.ConsoleUtil;
import revature.tariqkhan.database.DatabaseConnection;
import revature.tariqkhan.datevalidator.DateValidator;
import revature.tariqkhan.datevalidator.DateValidatorUsingLocalDate;
import revature.tariqkhan.main.StartApp;
import revature.tariqkhan.model.User;

public class UserCRUDImpl implements UserCRUD {
	Boolean isCustomer;
	Scanner sc = new Scanner(System.in);

	public Boolean getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}
	
	@Override
	public void getUserInput() {
		String firstName;
		String lastName;
		String dob;
		String email;
		String phone = null;
		String password;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		DateValidator validator = new DateValidatorUsingLocalDate(dateFormatter);

		// prompt user for first name
		System.out.println("Please enter your first name: ");
		while (!sc.hasNext()) {
	        System.out.println("Invalid input");
	        sc.nextLine();
	    }
		firstName = sc.next();
		
		// prompt user for last name
		System.out.println("Please enter your last name: ");
		while (!sc.hasNext()) {
	        System.out.println("Invalid input");
	        sc.nextLine();
	    }
		lastName = sc.next();
		
		// prompt user for date of birth
		System.out.println("Please enter your date of birth [year-month-day]: ");
		while (!sc.hasNext()) {
	        System.out.println("Invalid date input");
	        sc.nextLine();
	    }
		// Validate date of birth
		dob = sc.next();
		while (!validator.isValid(dob)) {
			System.out.println("Invalid date of birth");
			System.out.println("Please enter your date of birth: ");
			dob = sc.next();
		}
		
		// prompt user for email
		System.out.println("Please enter your email: ");
		while (!sc.hasNext()) {
	        System.out.println("Invalid input");
	        sc.nextLine();
	    }
		email = sc.next();
		
		// prompt user for optional phone number
		System.out.println("Enter an optional phone number [y/n]: ");
		String choice = sc.next();
		
		if (choice.equalsIgnoreCase("n")) {
			phone = null;
		} else {
	        System.out.println("Valid phone number pattern is (xxx)xxx-xxxx or xxx-xxx-xxxx or 'n' to skip: ");
			phone = sc.next();
			Pattern pattern = Pattern.compile("^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
		    Matcher matcher = pattern.matcher(phone);

			while (!matcher.matches() && !phone.equals("n")) {
		        System.out.println("Valid phone number pattern is (xxx)xxx-xxxx or xxx-xxx-xxxx or 'n' to skip: ");
				phone = sc.next();
				matcher = pattern.matcher(phone);
		    }
			
		}
		
		// Prompt for password
		String pwdMessage = "Enter a password: ";
		Console cons = System.console();
		ConsoleUtil util = new ConsoleUtil();
		
		// for non eclipse console to mask the password
		if (cons != null) {
			password = util.getPasswordMasked(cons, pwdMessage);
		} else {       
	        // Protect user's password. The generated value can be stored in DB.
			System.out.println(pwdMessage);
			password = sc.next();
		}

		User newUser = new User(firstName, lastName, dob, email, phone, password);
		System.out.println(newUser.toString());
		addUserAccount(newUser);	
		StartApp.userOrCustomer();
	}

	@Override
	public void addUserAccount(User user) {
		try (Connection conn = DatabaseConnection.dbConnection()) {
			String sql = "INSERT INTO users("
					+ "firstName,"
					+ "lastName,"
					+ "dob,"
					+ "email,"
					+ "phone,"
					+ "password) "
					+ "VALUES(?, ?, ?, ?, ?, crypt(?, gen_salt('bf')))";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setDate(3, java.sql.Date.valueOf(user.getDob()));
			stmt.setString(4, user.getEmail());
			stmt.setString(5, user.getPhone());
			stmt.setString(6, user.getPassword());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void userLogin(String email, String pwd) {
		try (Connection conn = DatabaseConnection.dbConnection()) {
			String sql = "SELECT * FROM users AS u "
					+ "FULL OUTER JOIN customer AS c "
					+ "ON u.userid = c.userid "
					+ "WHERE email= ? AND password = crypt(?, password)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, pwd);
			ResultSet rs = stmt.executeQuery();
			int custIdx  = rs.findColumn("customerAcct");
			boolean isCustomer = false;
			boolean isRegistered = rs.isBeforeFirst();
			int userID = 0;
			String firstName = "";
			
			// if user exist
			if (!isRegistered) {
				System.out.println("\n*** No user was found matching those login credentials ***");
			} else if (isRegistered) {
				while (rs.next()) {
					userID = rs.getInt(1);
					firstName = rs.getString(2);
					isCustomer = rs.getBoolean(custIdx);
				}
				
				String resp = null;
				
				 if (isCustomer) {
					 CustomerCRUDImpl customer = new CustomerCRUDImpl();
					 customer.customerOptions(userID, firstName);
				 } else if (!isCustomer) {	
					// Welcome existing user/customer
					System.out.println("\nWelcome back " + firstName + "!");
					do {
					System.out.println("\nWould you like to sign up for an account [y/n]? ");
					resp = sc.next();
					} while (!resp.equals("y") | !resp.equals("n"));
					
					if (resp.equals("y")) {
						customerAcct(userID);
					} else {
						StartApp.userOrCustomer();
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void customerAcct(int userID) {
		try (Connection conn = DatabaseConnection.dbConnection()) {
			String successMsg = "\nYou have successfully registered for a Customer Account\n"
					+ "You will notified shortly if your customer account is approved\n"
					+ "Thank you for using Revature Bank!";
			String sql = "INSERT INTO customer (userID) VALUES (?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			stmt.executeUpdate();
			System.out.println(successMsg);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
