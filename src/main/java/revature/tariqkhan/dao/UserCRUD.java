package revature.tariqkhan.dao;

import revature.tariqkhan.model.User;

public interface UserCRUD {
	public void getUserInput();
	public void userLogin(String email, String pwd);
	public void addUserAccount(User user);
	public void customerAcct(int userID);
}
