import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager
{
    private Connection con;
    private Scanner sc;
    public AccountManager(Connection con, Scanner sc)
    {
        this.con = con;
        this.sc = sc;
    }

    public void money_transfer(long sender_Account_num)
            throws SQLException
    {
        System.out.println("Enter the Account Number of Reciever");
        long rec_acc_num = sc.nextLong();
        System.out.println("Enter the Amount to Deposit");
        double deposit_amt = sc.nextDouble();
        System.out.println("Enter the Security Pin");
        String sec_pin = sc.next();

        try
        {
            con.setAutoCommit(false);
            if(sender_Account_num != 0 && rec_acc_num != 0)
            {
                String query = "SELECT * FROM accounts WHERE acc_num = ? AND sec_pin = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1,sender_Account_num);
                pst.setString(2,sec_pin);
                ResultSet res = pst.executeQuery();

                if(res.next())
                {
                    double curr_bal = res.getDouble("balance");

                    if(deposit_amt <= curr_bal)
                    {
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acc_num = ?";
                        PreparedStatement credit_pst = con.prepareStatement(credit_query);
                        credit_pst.setDouble(1,deposit_amt);
                        credit_pst.setLong(2,rec_acc_num);
                        int affectedRow1 = credit_pst.executeUpdate();

                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE acc_num = ?";
                        PreparedStatement debit_pst = con.prepareStatement(debit_query);
                        debit_pst.setDouble(1,deposit_amt);
                        debit_pst.setLong(2,sender_Account_num);
                        int affectedRow2 = debit_pst.executeUpdate();

                        if(affectedRow1 > 0 && affectedRow2 > 0)
                        {
                            System.out.println("Rs : "+deposit_amt+" Transferred Successfull");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }
                        else
                        {
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);

                        }

                    }
                    else
                    {
                        System.out.println("Insufficient balance");
                    }

                }
                else
                {
                    System.out.println("Invalid Pin");
                }

            }
            else
            {
                System.out.println("Invalid Account Number");
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        con.setAutoCommit(true);

    }


    public void credit_money(long Account_num)
    throws SQLException
    {
        System.out.println("Enter the Amount to credit");
        double credit_amt = sc.nextDouble();
        System.out.println("Enter the Security Pin");
        String sec_pin = sc.next();

        try {
            con.setAutoCommit(false);

            if (Account_num != 0) {
                String query = "SELECT * FROM accounts WHERE acc_num = ? AND sec_pin = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1, Account_num);
                pst.setString(2, sec_pin);

                ResultSet res = pst.executeQuery();

                if (res.next())
                {
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acc_num = ?";
                    PreparedStatement pst2 = con.prepareStatement(credit_query);
                    pst2.setDouble(1, credit_amt);
                    pst2.setLong(2, Account_num);
                    int rowAffected = pst.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Rs " + credit_amt + " Credited  Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }
            }
            else
            {
                System.out.println("Invalid Pin");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void debit_money(long Account_num)
    throws SQLException
    {
        System.out.println("Enter the Amount to withdraw");
        double withdraw_amt = sc.nextDouble();
        System.out.println("Enter the Security Pin");
        String sec_pin = sc.next();

        try
        {
            con.setAutoCommit(false);

            if(Account_num != 0)
            {
                String query = "SELECT * FROM accounts WHERE acc_num = ? AND sec_pin = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1,Account_num);
                pst.setString(2,sec_pin);

                ResultSet res = pst.executeQuery();

                if(res.next())
                {
                    double curr_bal = res.getDouble("balance");

                    if(withdraw_amt <= curr_bal )
                    {
                        String credit_query = "UPDATE accounts SET balance = balance - ? WHERE acc_num = ?";
                        PreparedStatement pst2 = con.prepareStatement(credit_query);
                        pst2.setDouble(1,withdraw_amt);
                        pst2.setLong(2,Account_num);
                        int rowAffected = pst.executeUpdate();
                        if(rowAffected > 0)
                        {
                            System.out.println("Rs "+withdraw_amt+" Withdraw Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }
                        else
                        {
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }
                    else
                    {
                        System.out.println("Insufficient Balance");
                    }
                }
                else
                {
                    System.out.println("Invalid Pin!");
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }


    public void check_bal(long Account_num)
    {
        System.out.println("Enter the Security Pin");
        String sec_pin = sc.next();

        try
        {

            String query = "SELECT balance FROM accounts WHERE acc_num = ? AND sec_pin = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1,Account_num);
            pst.setString(2,sec_pin);
            ResultSet res = pst.executeQuery();

            if(res.next())
            {
                double bal = res.getDouble("balance");
                System.out.println("Balance : "+ bal);
            }
            else
            {
                System.out.println("Invalid Pin");
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }
}
