package revature.tariqkhan.main;
import java.util.Scanner;

import revature.tariqkhan.dao.UserCRUDImpl;

public class StartApp {
	private static Scanner sc = new Scanner(System.in);
	private static String response = "";
	UserCRUDImpl userLogin = new UserCRUDImpl();

	public static void userOrCustomer() {
		while (!response.equals("q")) {
			String greeting = "\nPlease select from the following option by entering the option number:\n"
					+ "[1] Login\n"
					+ "[2] New User\n"
					+ "[q] Quit Program";
			System.out.println(greeting);
			response = sc.next();
	
			switch (response) {
				case "1":
					System.out.println("\nEnter your email: ");
					String email = sc.next();
					System.out.println("Enter your password: ");
					String pwd = sc.next();
					UserCRUDImpl userLogin = new UserCRUDImpl();
					userLogin.userLogin(email, pwd);
					
					break;
				case "2":
					System.out.println("Would you like to sign up as new user [y/n]? ");
					String signup = sc.next();
//					customerAcct(userID);
//					while (!signup.equals("y") | !signup.equals("n")) {
//						System.out.println("Please enter [y/n]? ");
//						signup = sc.next();
//					}
					
					if (signup.equals("y")) {
						UserCRUDImpl userLogin1 = new UserCRUDImpl();
						userLogin1.getUserInput();
					}
					
					break;
				case "q":
					sc.close();
					System.out.println("\nGood Bye!");
					System.exit(0);
				default:
					break;
			}
		}
	}
}
