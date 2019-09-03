import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ClientClass {

	
		
		public static void main(String[] args) {
			ServerClass obj =new ServerClass();
			System.out.println("Welcome to ICICI Bank");	
			System.out.println("Press 0 for register and 1 for login");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int val=0;
			try {
				val = Integer.parseInt(br.readLine());
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			switch(val)
			{

			case 0:
			System.out.println("Welcome to Registration Page");
				try {
					obj.register();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
			case 1:
				try {
					obj.login();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
	

}
