package revature.tariqkhan.dao;

import java.math.BigDecimal;

public interface CustomerCRUD {
	public void customerOptions(int userID, String firstName);
	public void depositFunds(int userID, BigDecimal amnt);
}
