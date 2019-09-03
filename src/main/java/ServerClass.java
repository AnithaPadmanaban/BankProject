import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerClass {

	
	
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void register() throws IOException, SQLException {
		System.out.println("Create UserName");
		String userName = br.readLine();
		System.out.println("Create Password");
		int password = Integer.parseInt(br.readLine());
		System.out.println("Enter Account Number");
		int accountNo = Integer.parseInt(br.readLine());
		System.out.println("Enter Initial Amount For Registration");
		int initialAmount = Integer.parseInt(br.readLine());

		Connection con = ConnectionBankUtil.getConnection();

		String sql = "insert into register_table(username,user_password,account_no) values(?,?,?)";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, userName);
		pst.setInt(2, password);
		pst.setInt(3, accountNo);
		int rows = pst.executeUpdate();
		System.out.println(rows);
		registerLogin(accountNo, initialAmount, con);

		System.out.println("Registered Successfully");
		System.out.println("Do you want to Login,Press Yes to login else press No to exit");
		String response=br.readLine();
		
		if("Yes".equalsIgnoreCase(response))
		{
		login();
		}
	
	else if("No".equalsIgnoreCase(response))
	{
		System.out.println("Thank You!!!");
	}
	
	}	

	public void login() throws IOException, SQLException

	{
		Connection con = ConnectionBankUtil.getConnection();
		System.out.println("Wecome to Login Page");

		System.out.println("Enter UserName");
		String userName = br.readLine();
		System.out.println("Enter Password");
		int password = Integer.parseInt(br.readLine());

		String sql = "select *from register_table where username=? and user_password=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, userName);
		pst.setInt(2, password);
		ResultSet rs = pst.executeQuery();

		if (rs.next()) {

			System.out.println("Hello " + userName + ",What do you like to perform");
			System.out.println("");
			System.out.println("=====================================================");
			System.out.println("Deposit------------>press 1");
			System.out.println("withdraw----------->press 2");
			System.out.println("Balance Enquiry---->press 3");
			System.out.println("Fund Transfer------>press 4");
			System.out.println("=====================================================");

			int inputVal = Integer.parseInt(br.readLine());
			switch (inputVal) {
			case 1:
				depositProcess();
				break;

			case 2:
				withdrawProcess();
				break;
				
			case 3:
				viewBalanceProcess();
				break;
				
			case 4:
				fundTransferProcess();
				break;

			}
		}

		else {
			System.out.println("Invalid username or password");
			System.out.println("Do you Want to Register New User Press Yes ,for Continue Login Press no,for Exit Press exit ");
			String response=br.readLine();
			if("yes".equalsIgnoreCase(response))
			{
				register();
			}
			else if("no".equalsIgnoreCase(response))
			{
				login();
			}
			
			else if("exit".equalsIgnoreCase(response))
			{
				System.out.println("Thank You!!");
			}
		}
		
	}

	
	public void fundTransferProcess() throws NumberFormatException, IOException, SQLException {
		debitAccountProcess();
		System.out.println("================================================");
		System.out.println("Do You Want to Continue Press yes else press no");
		String response=br.readLine();
		if("yes".equalsIgnoreCase(response))
		{
			login();
		}
		
		else  if("no".equalsIgnoreCase(response))
		{
			System.out.println("Thank You!!");
		}
		
	}

	public void debitAccountProcess() throws IOException, SQLException {
		System.out.println("Enter Account Number to Debit");
		int depitAccountNo = Integer.parseInt(br.readLine());
		
		
		System.out.println("Enter Transaction Amount");
		int transactionAmount = Integer.parseInt(br.readLine());
		
		Connection con = ConnectionBankUtil.getConnection();

		String sql = "select account_balance from login_table where account_no=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setInt(1, depitAccountNo);

		int balance = 0;
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			balance = rs.getInt(1);
		}

		int totalBalanceDebit = balance - transactionAmount;
		creditAccountProcess(transactionAmount);
		updateBalance(depitAccountNo, con, totalBalanceDebit);
		 System.out.println("Transaction successfully completed");
		System.out.println("Your Avaiable Balance is:"+totalBalanceDebit);
	}

	public void creditAccountProcess(int transactionAmount) throws IOException, SQLException {
		System.out.println("Enter Account Number to Credit");
		int creditAaccountNo = Integer.parseInt(br.readLine());
		
		
		
		Connection con = ConnectionBankUtil.getConnection();

		String sql = "select account_balance from login_table where account_no=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setInt(1, creditAaccountNo);

		int balance = 0;
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			balance = rs.getInt(1);
		}

		int totalBalanceCredit = balance + transactionAmount;
		updateBalance(creditAaccountNo, con, totalBalanceCredit);
	}

	
	
	public void depositProcess() throws NumberFormatException, IOException, SQLException {
		System.out.println("Enter Account Number");
		int accountNo = Integer.parseInt(br.readLine());
		System.out.println("Enter Amount to Deposit");
		int amount = Integer.parseInt(br.readLine());

		Connection con = ConnectionBankUtil.getConnection();

		String sql = "select account_balance from login_table where account_no=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			pst.setInt(1, accountNo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int balance = 0;
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			balance = rs.getInt(1);
		}

		int totalBalanceDeposit = balance + amount;

		updateBalance(accountNo, con, totalBalanceDeposit);
		System.out.println("Deposit Success");
		System.out.println("Your Available Balance is:"+totalBalanceDeposit);
		System.out.println("================================================");
		System.out.println("Do You Want to Continue Press yes else press no");
		String response=br.readLine();
		if("yes".equalsIgnoreCase(response))
		{
			login();
		}
		
		else  if("no".equalsIgnoreCase(response))
		{
			System.out.println("Thank You!!");
		}
	}

	public void withdrawProcess() throws NumberFormatException, IOException, SQLException {
		System.out.println("Enter Account Number");
		int accountNo = Integer.parseInt(br.readLine());
		System.out.println("Enter Amount to Withdraw");
		int amount = Integer.parseInt(br.readLine());

		Connection con = ConnectionBankUtil.getConnection();

		String sql = "select account_balance from login_table where account_no=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setInt(1, accountNo);

		int balance = 0;
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			balance = rs.getInt(1);
		}

		int totalBalanceWithdraw = balance - amount;
		updateBalance(accountNo, con, totalBalanceWithdraw);

		System.out.println("Withdraw Success");
		System.out.println("Your Available Balance is:"+totalBalanceWithdraw);
		System.out.println("================================================");
		System.out.println("Do You Want to Continue Press yes else press no");
		String response=br.readLine();
		if("yes".equalsIgnoreCase(response))
		{
			login();
		}
		
		else  if("no".equalsIgnoreCase(response))
		{
			System.out.println("Thank You!!");
		}
	}

	
	public void viewBalanceProcess() throws NumberFormatException, IOException, SQLException {
		System.out.println("Enter Account Number");
		int accountNo = Integer.parseInt(br.readLine());
		Connection con = ConnectionBankUtil.getConnection();

		String sql = "select account_balance from login_table where account_no=?";
		
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setInt(1, accountNo);
		ResultSet rs = pst.executeQuery();
		int balance = 0;
		if (rs.next()) {
			balance = rs.getInt(1);
		}
		
		System.out.println("Your Available Balance : "+balance);
		System.out.println("===================================");
		System.out.println("Do You Want to Continue Press yes else press no");
		String response=br.readLine();
		if("yes".equalsIgnoreCase(response))
		{
			login();
		}
		
		else  if("no".equalsIgnoreCase(response))
		{
			System.out.println("Thank You!!");
		}

	}

	
	public void registerLogin(int accountNo, int initDeposit, Connection con) throws SQLException {
		String sql1 = "insert into login_table(account_no,account_balance) values(?,?)";
		PreparedStatement 
		
		pst1 = con.prepareStatement(sql1);
		pst1.setInt(1, accountNo);
		pst1.setInt(2, initDeposit);
		pst1.executeUpdate();
	}

	public void updateBalance(int accountNo, Connection con, int totalBalance) throws SQLException {
		String sql = "update login_table set account_balance=? where account_no=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setInt(1, totalBalance);
		pst.setInt(2, accountNo);
		int rows = pst.executeUpdate();
	}
}
