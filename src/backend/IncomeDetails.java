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
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author BCz
 */
public class IncomeDetails {

    public IncomeDetails() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:datas.sqlite");
        stmt = connection.createStatement();
        list = FXCollections.observableArrayList();
        
    }
    
    public void insertIncome(String date, String category, String amount, String groupe) throws Exception{
        stmt.executeUpdate("Insert into IncomeDetails ("
                
                + "Date, Category, Amount, groupe "
                
                + ") Values "
                + "("
                + "'"+date+"',"
                + "'"+category+"',"
                + "'"+amount+"',"
                + "'"+groupe+"'"
                + "); "
                + "");
            stmt.executeUpdate("Insert into DayBook ("
                        + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                        + ") Values "
                        + "("
                        + "'"+getMaxId()+"',"
                        + "'"+category+"',"
                        + "'"+date+"',"
                        + "'Direct Income',"
                        + "'"+amount+"',"
                        + "'',"
                        + "''"
                        + "); "
                        + ""
                );
            insertDayBookBalance(date);
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
    
    private String getMaxId() throws SQLException {
        String id = "";
        rs = stmt.executeQuery("select max(id) as lastId from IncomeDetails");
        while(rs.next()) {
            id = rs.getString("lastId");
        }
        return id;
    }
    
    public void close() throws SQLException {
        connection.close();
    }
    
    public Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ObservableList list;
}

