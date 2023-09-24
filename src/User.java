import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User
{
    private Connection con;
    private Scanner sc;
    public User(Connection con, Scanner sc)
    {
        this.con = con;
        this.sc = sc;
    }

    public void register()
    {
        System.out.println("Enter the Full Name");
        String full_name = sc.next();
        System.out.println("Enter the Email");
        String email = sc.next();
        System.out.println("Enter Password");
        String password = sc.next();

        if(user_exists(email))
        {
            System.out.println("Email Already Exists");
            return;
        }
        String query = "INSERT INTO user1(full_name, email, password) VALUES (?, ?, ?)";
        try{
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,full_name);
            pst.setString(2,email);
            pst.setString(3,password);
            int affectedRows = pst.executeUpdate();
            if(affectedRows > 0)
            {
                System.out.println("Registration Successfull");
            }
            else
            {
                System.out.println("Registration Failed");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }

    public String login()
    {
        System.out.println("Enter email");
        String email = sc.next();
        System.out.println("Enter password");
        String password = sc.next();

        String query = "SELECT * FROM user1 WHERE email = ? AND password = ?";
        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,email);
            pst.setString(2,password);
            ResultSet res = pst.executeQuery();

            if (res.next())
            {
                return email;
            }
            else
            {
                return null;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    return null;

    }

    public boolean user_exists(String email)
    {
        String query = "SELECT * FROM user1 WHERE email = ?";
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
