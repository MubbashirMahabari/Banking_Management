import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "Mubbashir@91";


    public static void main(String[] args)
            throws SQLException, ClassNotFoundException
    {
        try
        {
           Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("drivers loaded");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        try
        {
            Connection con = DriverManager.getConnection(url,username,password);
            System.out.println("connection Established");
            Scanner sc = new Scanner(System.in);

            User user = new User(con, sc);
            Accounts acc = new Accounts(con ,sc);
            AccountManager acc_mang = new AccountManager(con, sc);

            String email;
            long Account_num;

            while(true)
            {
                System.out.println("***** WELCOME TO BANKING SYSTEM *****");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter the choice");
                int ch = sc.nextInt();

                switch (ch)
                {
                    case 1: user.register();
                            break;

                    case 2: email = user.login();
                            if(email != null)
                            {
                                System.out.println("User Logged In");
                                if(!acc.acc_exists(email))
                                {
                                    System.out.println("1. Open New Account");
                                    System.out.println("2. Exit");
                                    System.out.println("Enter the choice");
                                    int ch1 = sc.nextInt();

                                    if(ch1 == 1)
                                    {
                                        Account_num = acc.open_acc(email);
                                        System.out.println("Account Created Successfull");
                                        System.out.println("Account Number is " +Account_num);
                                    }
                                    else
                                    {
                                        break;
                                    }

                                }
                                Account_num = acc.get_acc_num(email);
                                int ch3 = 0;
                                while(ch3 != 5)
                                {
                                    System.out.println("1. Debit Amount");
                                    System.out.println("2. Credit Amount");
                                    System.out.println("3. Transfer Money");
                                    System.out.println("4. Check Balance");
                                    System.out.println("5. Log Out");
                                    System.out.println("Enter you choice");
                                    ch3 = sc.nextInt();

                                    switch (ch3)
                                    {
                                        case 1: acc_mang.debit_money(Account_num);
                                        break;

                                        case 2:acc_mang.credit_money(Account_num);
                                        break;

                                        case 3: acc_mang.money_transfer(Account_num);
                                        break;

                                        case 4: acc_mang.check_bal(Account_num);
                                        break;

                                        case 5: break;

                                        default:
                                            System.out.println("INVALID CHOICE!");
                                    }

                                }
                            }
                            else
                            {
                                System.out.println("Incoorect Email or Password");
                            }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM");
                        return;

                    default:
                        System.out.println("INVALID Choice!");
                        break;
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}