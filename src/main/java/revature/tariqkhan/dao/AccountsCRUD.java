package revature.tariqkhan.dao;

import java.math.BigDecimal;

public interface AccountsCRUD {
	public void addToAccounts(int userID, BigDecimal amnt);
	public boolean acctStatus(int userID);
	public boolean verifyAccountNum(int userID, int acctID);
	public BigDecimal getBalance(int userID, int accountID);
	public void getAccountInfo(int userID, int accountID);
	public void makeDeposit(int userID, int acctID, BigDecimal amount);
	public void makeWithdrawal(int userID, int acctID, BigDecimal amount);
}
