package revature.tariqkhan.consoleutil;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUtil {
	
    /**
     * Handles console input when running outside of Eclipse.
     * 
     * @param cons the console to use in order to receive input
     * @param msg the prompt message
     * @return the password input by the user
     */
    public String getPasswordMasked(Console cons, String msg) {
        char[] passwd;
        while (true) {
            passwd = cons.readPassword("%s", msg);
            if (passwd != null) {
                if (passwd.length > 0) {
                    return new String(passwd);
                } else {
                    System.out.println("Invalid input\n");
                }
            }
        }
    }
    
    /**
     * Handles console input when running inside of Eclipse; See Eclipse bug
     * #122429.
     * 
     * @param msg the prompt message
     * @return the password input by the user
     * @throws IOException if password is zero-length
     */
    public String getPasswordWithinEclipse(String msg) 
            throws IOException {
        // In Eclipse IDE
        System.out.print(msg);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        String password = reader.readLine();
        if (password != null) {
            if (password.length() <= 0) {
                System.out.println("Invalid input\n");
                throw new IOException("Error reading in password");
            }
        }
        return password;
    }
}
