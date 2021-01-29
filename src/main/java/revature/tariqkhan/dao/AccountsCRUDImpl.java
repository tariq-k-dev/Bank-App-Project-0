package revature.tariqkhan.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import revature.tariqkhan.database.DatabaseConnection;

public class AccountsCRUDImpl implements AccountsCRUD {
//	private int accountId;
//	private BigDecimal balance;
	
	@Override
	public void addToAccounts(int userID, BigDecimal amnt) {
		// Add user to accounts table
		try (Connection conn = DatabaseConnection.dbConnection()) {
			String sql = "INSERT INTO accounts (userID, balance)"
					+ "VALUES (?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			stmt.setBigDecimal(2, amnt);
			stmt.executeUpdate();
			String confirmation = "\nYour request for a new account has been submitted.\n"
					+ "You will be notified of your account status shortly.\n"
					+ "Thank you for banking with Revature Bank!\n";
			System.out.println(confirmation);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean acctStatus(int userID) {
		// Returns if account is active
		try (Connection conn = DatabaseConnection.dbConnection()) {
			String sql = "SELECT isActive FROM accounts WHERE acctID = ? AND isActive = true";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			ResultSet rs = stmt.executeQuery();
			boolean isActive = rs.isBeforeFirst();
			
			return isActive;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public BigDecimal getBalance(int userID, int accountID) {
		try (Connection conn = DatabaseConnection.dbConnection()) {
			BigDecimal balance = null;
			// int accountID = 0;
			String sql = "SELECT balance FROM accounts WHERE userID = ? AND accountID = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, userID);
			stmt.setInt(2, accountID);
			ResultSet rs = stmt.executeQuery();
			boolean isRecord = rs.isBeforeFirst();
			
			if (isRecord) {
				while (rs.next()) {
					// accountID = rs.getInt("accountID");
					balance = rs.getBigDecimal("balance");
				}
			}
			
			return balance;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void getAccountInfo(int userID, int acctID) {
		try (Connection conn = DatabaseConnection.dbConnection()) {
			int accountID = 0;
			String firstName = "";
			String lastName = "";
			BigDecimal balance = null;
			String sql = "SELECT firstName, lastName, accountID, balance "
					+ "FROM users AS u "
					+ "JOIN accounts AS a "
					+ "ON a.userid = u.userid "
					+ "WHERE u.userID = ? AND a.accountID = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, userID);
			stmt.setInt(2, acctID);
			ResultSet rs = stmt.executeQuery();
			boolean isRecord = rs.isBeforeFirst();
			
			if (isRecord) {
				while (rs.next()) {
					firstName = rs.getString("firstName");
					lastName = rs.getString("lastName");
					accountID = rs.getInt("accountID");
					balance = rs.getBigDecimal("balance");
					String details = "\n[ Customer Name: " + firstName + " " + lastName + " | "
							+ "User ID: " + userID + " | Account ID: " + accountID + " | Balance: $" + balance + " ]";
					System.out.println(details);
				}
			} else {
				System.out.println("No account information was available for that account");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void makeDeposit(int userID, int acctID, BigDecimal amount) {
		if (verifyAccountNum(userID, acctID)) {
			try (Connection conn = DatabaseConnection.dbConnection()) {
				System.out.print("\nOriginal account details:");
				getAccountInfo(userID, acctID);
				BigDecimal currBalance = getBalance(userID, acctID);
				BigDecimal updatedBalance = amount.add(currBalance);
				String sql = "UPDATE accounts SET balance = ? WHERE accountID = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setBigDecimal(1, updatedBalance);
				stmt.setInt(2, acctID);
				stmt.executeUpdate();
				System.out.print("\nUpdated account details:");
				getAccountInfo(userID, acctID);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("That account number could not be verified\nPlease try again");
		}
	}

	@Override
	public boolean verifyAccountNum(int userID, int acctID) {
		try (Connection conn = DatabaseConnection.dbConnection()) {
			String sql = "SELECT * FROM accounts WHERE userID = ? AND accountID = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			stmt.setInt(2, acctID);
			ResultSet rs = stmt.executeQuery();
			boolean isRecord = rs.isBeforeFirst();
			
			return isRecord;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void makeWithdrawal(int userID, int acctID, BigDecimal amount) {
		try (Connection conn = DatabaseConnection.dbConnection()) {
			BigDecimal currBalance = getBalance(userID, acctID);
			if (currBalance.compareTo(amount) >= 0) {
				System.out.print("\nOriginal account details:");
				getAccountInfo(userID, acctID);
				BigDecimal newBalance = currBalance.subtract(amount);
				String sql = "UPDATE accounts SET balance = ? WHERE userID = ? AND accountID = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setBigDecimal(1, newBalance);
				stmt.setInt(2, userID);
				stmt.setInt(3, acctID);
				stmt.executeUpdate();
				System.out.print("\nUpdated account details:");
				getAccountInfo(userID, acctID);
			} else {
				System.out.println("\n*** There is not enough money in your account to withdraw $" + amount + " ***");
				System.out.println("\nThe current balance of this account is $" + currBalance);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
