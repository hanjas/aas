/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.tableModel.InsertOtherDetailsTableModel;

/**
 *
 * @author BCz
 */
public class SingleBuyerDetails {

    public SingleBuyerDetails(String buyer) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:datas.sqlite");
        stmt = connection.createStatement();
        list = FXCollections.observableArrayList();
        this.buyer = buyer;
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+buyer+" ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "BillNo TEXT , "
                + "Date Date , "
                + "BoxCount TEXT , "
                + "BillAmt TEXT, "
                + "OldBal TEXT, "
                + "Total TEXT, "
                + "CashPaid TEXT, "
                + "Balance TEXT"
                + "); "
        );
    }
    public void insertBuyerDetails(String id, String sellerName, String billNo, String date, String boxCount,
            String billAmt, String oldBal, String total, String cashPaid, String balance) throws Exception{
        if(id.equals("")) {
            stmt.executeUpdate("Insert into "+buyer+" ("

                    + "BillNo, Date, BoxCount, BillAmt, OldBal, Total, CashPaid, Balance "

                    + ") Values "
                    + "("
                    + "'"+billNo+"',"
                    + "'"+date+"',"
                    + "'"+boxCount+"',"
                    + "'"+billAmt+"',"
                    + "'"+oldBal+"',"
                    + "'"+total+"',"
                    + "'"+cashPaid+"',"
                    + "'"+balance+"'"
                    + "); "
                    + "");



            if(!billAmt.equals("")) {
                stmt.executeUpdate("Insert into AllBuyerDetails ("
                        + "BuyerId, SellerName, Name, BillNo, Date, BoxCount, BillAmt, OldBal, Total, CashPaid, Balance "
                        + ") Values "
                        + "("
                        + "'"+getMaxId()+"',"
                        + "'"+sellerName+"',"
                        + "'"+buyer.replaceAll("_", " ")+"',"
                        + "'"+billNo+"',"
                        + "'"+date+"',"
                        + "'"+boxCount+"',"
                        + "'"+billAmt+"',"
                        + "'"+oldBal+"',"
                        + "'"+total+"',"
                        + "'"+cashPaid+"',"
                        + "'"+balance+"'"
                        + "); "
                        + "");
                if(!cashPaid.equals("")) {
                    if(!cashPaid.equals("0")){
                        stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+getMaxId()+"',"
                                + "'"+buyer.replaceAll("_", " ")+"',"
                                + "'"+date+"',"
                                + "'Cash Paid',"
                                + "'"+cashPaid+"',"
                                + "'',"
                                + "''"
                                + "); "
                                + ""
                        );
                    }
                }
                rs = stmt.executeQuery("select * from AllSellerDetails where billNo='"+billNo+"' and Name='"+sellerName+"'");
                float totalBox=0, totalBoxSold=0;
                int saleAmt=0;
                while(rs.next()){
                    totalBox = rs.getFloat("totalBox");
                    totalBoxSold = rs.getFloat("totalBoxSold");
                    saleAmt = rs.getInt("saleAmt");
                }
                stmt.executeUpdate("Update AllSellerDetails set "
                        + "totalBoxSold='"+(totalBoxSold+Float.parseFloat(boxCount))+"',"
                        + "saleAmt = '"+(saleAmt+Integer.parseInt(billAmt))+"',"
                        + "remainingBox = '"+(totalBox-(totalBoxSold+Float.parseFloat(boxCount)))+"'"
                        + "where Name = '"+sellerName+"' and billNo='"+billNo+"'");
                if(!date.equals(""))
                    updateDB(date, balance);
                insertDayBookBalance(date);
                stmt.executeUpdate("UPDATE Buyers SET billNo = '"+billNo+"', balance='"+balance+"' where name = '"+buyer.replace("_", " ")+"'");
            } 
        }
    }
    
    private void updateDB(String fromDate, String todayBalance) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateFormat.parse( fromDate ) );
        java.util.Date lastDate=null;
        rs = stmt.executeQuery("select max(date) as lastDate from "+buyer);
        while(rs.next()) {
            if(!rs.getString("lastDate").equals(""))
                lastDate = dateFormat.parse(rs.getString("lastDate"));
        }
        if(lastDate!=null) {
            int balance = 0, total =0;
            String nextDate = "";
            List<String> oldBal =new ArrayList<String>();
            List<String> id =new ArrayList<String>();
            List<String> bal =new ArrayList<String>();
            List<String> tot =new ArrayList<String>();
            balance = Integer.parseInt(todayBalance);
            while(cal.getTime().before(lastDate)  && !lastDate.equals(null)) {
                oldBal.clear();
                id.clear();
                tot.clear();
                bal.clear();
                cal.add( Calendar.DATE, 1 );
                nextDate = dateFormat.format(cal.getTime());
                rs = stmt.executeQuery("select * from "+buyer+" where date = '"+nextDate+"' order by id");
                while(rs.next()) {
                    total = Integer.parseInt(todayBalance) + rs.getInt("billAmt");
                    balance = total - rs.getInt("cashPaid");
                    oldBal.add(todayBalance);
                    tot.add(total+"");
                    bal.add(balance+"");
                    id.add(rs.getString("id"));
                    todayBalance = balance+"";
                }
                for(int i=0; i<oldBal.size(); i++) {
                    stmt.executeUpdate("update "+buyer+" set oldBal='"+oldBal.get(i)+"', total='"+tot.get(i)+"', balance='"+bal.get(i)+"' where id="+id.get(i));
                    stmt.executeUpdate("update AllBuyerDetails set oldBal='"+oldBal.get(i)+"', total='"+tot.get(i)+"', balance='"+bal.get(i)+"' where buyerId="+id.get(i)+" and name = '"+buyer.replace("_", " ")+"'");
                }

            }
            if(cal.getTime().equals(lastDate)) {
                if(balance >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balance+"").replace("-", "")+"', toGive='' where name='"+buyer.replace("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balance+"").replace("-", "")+"', toGet='' where name='"+buyer.replace("_", " ")+"'");

            }
        }
    }
    
    public void insertDayBookBalance(String date) throws Exception {
        
        java.util.Date lastDate=null, today=null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        today = df.parse(LocalDate.now().toString());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateFormat.parse( date ) );
        rs = stmt.executeQuery("select max(date) as lastDate from DayBook ");
        while(rs.next()) {
            lastDate = df.parse(rs.getString("lastDate"));
        }
        while(cal.getTime().before(lastDate) || cal.getTime().equals(today) && !lastDate.equals(null)) {
            cal.add( Calendar.DATE, 1 );
            String nextDate = dateFormat.format(cal.getTime());
            int check =0, balance = 0;
            rs = stmt.executeQuery("select * from DayBook where date = '"+date+"'");
            while(rs.next()){
                balance = balance + rs.getInt("toGet") - rs.getInt("expence") - rs.getInt("nonExpence");
            }
            date = dateFormat.format(cal.getTime());
            rs = stmt.executeQuery("select * from DayBook where date = '"+nextDate+"' and name = 'Cash In Hand'");
            while(rs.next()) {
                check = 1;
            }
            if(check == 0)
                stmt.executeUpdate("Insert into DayBook ("
                    + "Name, Date, Category, ToGet, Expence, NonExpence "
                    + ") Values "
                    + "("
                    + "'Cash In Hand',"
                    + "'"+nextDate+"',"
                    + "'Cash In Hand',"
                    + "'"+balance+"',"
                    + "'',"
                    + "''"
                    + "); "
                    + ""
                );
            if(check == 1) {
                stmt.executeUpdate("UPDATE DayBook SET ToGet = '"+balance+"' where date= '"+nextDate+"' and name= 'Cash In Hand' and category = 'Cash In Hand'");
            }
        }
    }
    public void insertLossAndExcess(String billNo, String sellerName, String loss, String excess, String remainingBox) throws SQLException {
        stmt.executeUpdate("update AllSellerDetails set loss = '"+loss+"', Excess = '"+excess+"', remainingBox='"+remainingBox+"'  where name = '"+sellerName+"' and billNo = '"+billNo+"'");
    }
    
    public String getLastBalance(String date) throws SQLException {
        int id = getLastId(date);
        rs = stmt.executeQuery(" SELECT * FROM "+buyer+" WHERE ID = "+id );
        while(rs.next()) {
           return rs.getString("Balance");
        }
        return "0";
    }
    public int getLastId(String date) throws SQLException {
        int id = 1;
        rs = stmt.executeQuery("select * from "+buyer+" where date <= '"+date+"' order by id desc limit 1");
        while(rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }
    public int getMaxId() throws SQLException {
        rs = stmt.executeQuery("select max(id) as lstid from "+buyer+"");
        while(rs.next()) {
            return rs.getInt("lstid");
        }
        return 0;
    }
    public String getPrevDate(String date) throws SQLException {
        String prevDate = "";
        rs = stmt.executeQuery("select date from "+buyer+" where date <= '"+date+"' order by id desc limit 1");
        while(rs.next()) {
            prevDate = rs.getString("date");
        }
        return prevDate;
    }
    public String getNextDate(String date) throws SQLException {
        String nextDate = "";
        rs = stmt.executeQuery("select date from "+buyer+" where date >= '"+date+"' order by id limit 1");
        while(rs.next()) {
            nextDate = rs.getString("date");
        }
        return nextDate;
    }
    
    public void removeData(String id) throws Exception {
        String date="", oldBal="";
        if(!id.equals("")) {
            rs = stmt.executeQuery("SELECT date FROM "+buyer+" WHERE ID = "+id);
            while(rs.next()) {
                date = rs.getString("date");
            }
            stmt.executeUpdate("delete from "+buyer+" where id="+id);
            stmt.executeUpdate("delete from AllBuyerDetails where BuyerId='"+id+"' and name='"+buyer.replaceAll("_", " ")+"'");
            stmt.executeUpdate("delete from DayBook where idd='"+id+"' and name='"+buyer.replaceAll("_", " ")+"' and category='Cash Paid'");
            oldBal = getLastBalance(date);
            if(Integer.parseInt(oldBal) > 0) 
                stmt.executeUpdate("update BalanceSheet set  toGet='"+oldBal+"', toGive=''  where Name='"+buyer.replaceAll("_", " ")+"'");
            else 
                stmt.executeUpdate("update BalanceSheet set  toGet='', toGive='"+oldBal.replaceAll("-", "")+"'  where Name='"+buyer.replaceAll("_", " ")+"'");
            stmt.executeUpdate("UPDATE Buyers SET billNo = '"+id+"', balance='"+oldBal+"' where name = '"+buyer.replace("_", " ")+"'");
            updateDB(date, oldBal);
        }
    }
    
    public void close() throws SQLException {
        connection.close();
    }
    
    public Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ObservableList list;
    private String buyer="";
}
