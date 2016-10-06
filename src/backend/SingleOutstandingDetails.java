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

/**
 *
 * @author BCz
 */
public class SingleOutstandingDetails {

    public SingleOutstandingDetails(String OutstanderName) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:datas.sqlite");
        stmt = connection.createStatement();
        list = FXCollections.observableArrayList();
        this.OutstanderName = OutstanderName;
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.OutstanderName+" ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Date Date , "
                + "Category TEXT , "
                + "OldBal TEXT , "
                + "ToGet TEXT, "
                + "ToGive TEXT, "
                + "Balance TEXT"
                + "); "
        );
    }
    
    public void insertOutstandingDetails(String date, String category, String oldBal, String toGet, String toGive, String balance) throws Exception{
        stmt.executeUpdate("Insert into "+OutstanderName+" ("
                
                + "Date, category, OldBal, ToGet, ToGive, Balance "
                
                + ") Values "
                + "("
                + "'"+date+"',"
                + "'"+category+"',"
                + "'"+oldBal+"',"
                + "'"+toGet+"',"
                + "'"+toGive+"',"
                + "'"+balance+"'"
                + "); "
                + "");
        
        stmt.executeUpdate("Insert into AllOutstandingDetails ("
                + "OutstanderId, Name, Date, category, OldBal, ToGet, ToGive, Balance "
                + ") Values "
                + "("
                + "'"+getMaxId()+"',"
                + "'"+OutstanderName.replaceAll("_", " ")+"',"
                + "'"+date+"',"
                + "'"+category+"',"
                + "'"+oldBal+"',"
                + "'"+toGet+"',"
                + "'"+toGive+"',"
                + "'"+balance+"'"
                + "); "
                + "");
        
        if(!date.equals(""))
            updateDB(date, balance);
        insertDayBook(getMaxId(), date, category, toGet, toGive);
        stmt.executeUpdate("Update Outstanding set lastId='"+getMaxId()+"', balance='"+balance+"' where name='"+OutstanderName.replace("_", " ")+"'");
        if(Integer.parseInt(balance)>=0) {
            stmt.executeUpdate("UPDATE OutstanderDetails SET toGet = '"+balance+"', toGive='' where name= '"+OutstanderName.replace("_", " ")+"'");
        } else {
            stmt.executeUpdate("UPDATE OutstanderDetails SET toGet='', toGive = '"+balance+"' where name= '"+OutstanderName.replace("_", " ")+"'");
        }
    }
    public void insertDayBook(int id, String date, String category, String toGet, String toGive) throws Exception {
        if(!date.equals("")) {
            if(!toGet.equals("")) {
                if(!toGet.equals("0")) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+OutstanderName.replaceAll("_", " ")+"',"
                                + "'"+date+"',"
                                + "'"+category+"',"
                                + "'"+toGet+"',"
                                + "'',"
                                + "''"
                                + "); "
                                + ""
                        );
                }
            }
            if(!toGive.equals("")) {
                if(!toGive.equals("0")) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+OutstanderName.replaceAll("_", " ")+"',"
                                + "'"+date+"',"
                                + "'"+category+"',"
                                + "'',"
                                + "'',"
                                + "'"+toGive+"'"
                                + "); "
                                + ""
                        );
                }
            }
            insertDayBookBalance(date);
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
    
    private void updateDB(String fromDate, String todayBalance) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateFormat.parse( fromDate ) );
        java.util.Date lastDate=null;
        rs = stmt.executeQuery("select max(date) as lastDate from "+OutstanderName);
        while(rs.next()) {
            if(!rs.getString("lastDate").equals(""))
                lastDate = dateFormat.parse(rs.getString("lastDate"));
        }
        if(lastDate != null) {
            int balance = 0;
            String nextDate = "";
            List<String> oldBal =new ArrayList<String>();
            List<String> id =new ArrayList<String>();
            List<String> bal =new ArrayList<String>();
            balance = Integer.parseInt(todayBalance);
            while(cal.getTime().before(lastDate) && lastDate != null) {
                oldBal.clear();
                id.clear();
                bal.clear();
                cal.add( Calendar.DATE, 1 );
                nextDate = dateFormat.format(cal.getTime());
                rs = stmt.executeQuery("select * from "+OutstanderName+" where date = '"+nextDate+"' order by id");
                while(rs.next()) {
                    balance = Integer.parseInt(todayBalance) + rs.getInt("ToGet") - rs.getInt("ToGive");
                    oldBal.add(todayBalance);
                    bal.add(balance+"");
                    id.add(rs.getString("id"));
                    todayBalance = balance+"";
                }
                for(int i=0; i<oldBal.size(); i++) {
                    stmt.executeUpdate("update "+OutstanderName+" set oldBal='"+oldBal.get(i)+"', balance='"+bal.get(i)+"' where id="+id.get(i));
                    stmt.executeUpdate("update AllOutstandingDetails set oldBal='"+oldBal.get(i)+"', balance='"+bal.get(i)+"' where outstanderid="+id.get(i)+" and name = '"+OutstanderName.replace("_", " ")+"'");

                }

            }
            if(cal.getTime().equals(lastDate)) {
                if(balance >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balance+"").replace("-", "")+"', toGet='' where name='"+OutstanderName.replace("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balance+"").replace("-", "")+"', toGive='' where name='"+OutstanderName.replace("_", " ")+"'");

            }
        }
    }
    
    public String getLastBalance(String date) throws SQLException {
        int id = getLastId(date);
        rs = stmt.executeQuery(" SELECT * FROM "+OutstanderName+" WHERE ID = "+id );
        while(rs.next()) {
           return rs.getString("Balance");
        }
        return "0";
    }
    public int getLastId(String date) throws SQLException {
        int id = 1;
        rs = stmt.executeQuery("select * from "+OutstanderName+" where date <= '"+date+"' order by id desc limit 1");
        while(rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }
    public int getNextId(String id) throws SQLException {
        rs = stmt.executeQuery("select id from "+OutstanderName+" where id > '"+id+"' limit 1");
        while(rs.next()) {
            return rs.getInt("id");
        }
        return Integer.parseInt(id);
    }
    public int getMaxId() throws SQLException {
        rs = stmt.executeQuery("select max(id) as lstid from "+OutstanderName+"");
        while(rs.next()) {
            return rs.getInt("lstid");
        }
        return 0;
    }
    
    public void deleteOutstandingData(String date, String id) throws Exception {
        stmt.executeUpdate("Delete from "+OutstanderName+" where id = '"+id+"'");
        stmt.executeUpdate("Delete from AllOutstandingDetails where OutstanderId = '"+id+"'");
        int oldBal = Integer.parseInt(getLastBalance(date));
        if(oldBal >=0)
            stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(oldBal+"").replace("-", "")+"', toGet='' where name='"+OutstanderName.replace("_", " ")+"'");
        else
            stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(oldBal+"").replace("-", "")+"', toGive='' where name='"+OutstanderName.replace("_", " ")+"'");
        updateDB(date, oldBal+"");
    }
    
    public void close() throws SQLException {
        connection.close();
    }
    
    public Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ObservableList list;
    private String OutstanderName="";

}
