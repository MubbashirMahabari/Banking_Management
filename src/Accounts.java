import java.sql.*;
import java.util.Scanner;
import java.util.stream.StreamSupport;

public class Accounts
{
    private Connection con;
    private Scanner sc;
    public Accounts(Connection con, Scanner sc)
    {
        this.con = con;
        this.sc = sc;
    }

    public long open_acc(String email)
    {
        if(!acc_exists(email))
        {
            String query = "INSERT INTO accounts(acc_num, full_name, email, balance, sec_pin) VALUES(?, ?, ?, ?)";
            System.out.println("Enter the Full Name");
            String full_name = sc.next();
            System.out.println("Enter Initial Amount");
            double balance = sc.nextDouble();
            System.out.println("Enter Security Pin");
            String sec_pin = sc.next();

            try
            {
                Long Account_num = generate_acc_num();

                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1,Account_num);
                pst.setString(2,full_name);
                pst.setString(3,email);
                pst.setDouble(4,balance);
                pst.setString(5,sec_pin);

                int RowAffected = pst.executeUpdate();
                if(RowAffected > 0)
                {
                    return Account_num;
                }
                else
                {
                    throw new RuntimeException("Account Creation failed!!");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist");
    }

    public long get_acc_num(String email)
    {
        String query = "SELECT acc_num FROM accounts WHERE email = ?";
        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,email);
            ResultSet res = pst.executeQuery();
            if(res.next())
            {
                return res.getLong("acc_num");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException("Email Doesn't Exists");
    }

    private long generate_acc_num()
    {
        String query = "SELECT acc_num FROM accounts ORDER BY acc_num DESC LIMIT 1";
        try
        {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(query);
            if(res.next())
            {
                long last_acc_num = res.getLong("acc_num");
                return last_acc_num + 1;
            }
            else
            {
                return 10000100;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean acc_exists(String email)
    {
        String query = "SELECT * FROM accounts WHERE email = ?";
        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,email);
            ResultSet res = pst.executeQuery();
            if(res.next())
            {
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
