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
 * @author USER
 */
public class SingleInvestmentDeptDetails {

    public SingleInvestmentDeptDetails(String InvestmentDeptName) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:datas.sqlite");
        stmt = connection.createStatement();
        list = FXCollections.observableArrayList();
        this.InvestmentDeptName = InvestmentDeptName;
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.InvestmentDeptName+" ("    // Sellers Table
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
    public void insertInvestmentDeptDetails(String date, String category, String oldBal, String toGet, String toGive, String balance) throws Exception{
        stmt.executeUpdate("Insert into "+InvestmentDeptName+" ("
                
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
        
        stmt.executeUpdate("Insert into AllInvestmentDeptDetails ("
                + "InvestmentDeptId, Name, Date, category, OldBal, ToGet, ToGive, Balance "
                + ") Values "
                + "("
                + "'"+getMaxId()+"',"
                + "'"+InvestmentDeptName.replaceAll("_", " ")+"',"
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
        stmt.executeUpdate("Update InvestmentDept set lastId='"+getMaxId()+"', balance='"+balance+"' where name='"+InvestmentDeptName.replace("_", " ")+"'");
        if(Integer.parseInt(balance)>=0) {
            stmt.executeUpdate("UPDATE InvestmentDeptDetails SET toGet = '"+balance+"', toGive='' where name= '"+InvestmentDeptName.replace("_", " ")+"'");
        } else {
            stmt.executeUpdate("UPDATE InvestmentDeptDetails SET toGet='', toGive = '"+balance+"' where name= '"+InvestmentDeptName.replace("_", " ")+"'");
        }
    }
    
    private void updateDB(String fromDate, String todayBalance) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateFormat.parse( fromDate ) );
        java.util.Date lastDate=null;
        rs = stmt.executeQuery("select max(date) as lastDate from "+InvestmentDeptName);
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
                rs = stmt.executeQuery("select * from "+InvestmentDeptName+" where date = '"+nextDate+"' order by id");
                while(rs.next()) {
                    balance = Integer.parseInt(todayBalance) + rs.getInt("ToGet") - rs.getInt("ToGive");
                    oldBal.add(todayBalance);
                    bal.add(balance+"");
                    id.add(rs.getString("id"));
                    todayBalance = balance+"";
                }
                for(int i=0; i<oldBal.size(); i++) {
                    stmt.executeUpdate("update "+InvestmentDeptName+" set oldBal='"+oldBal.get(i)+"', balance='"+bal.get(i)+"' where id="+id.get(i));
                    stmt.executeUpdate("update AllInvestmentDeptDetails set oldBal='"+oldBal.get(i)+"', balance='"+bal.get(i)+"' where investmentDeptId="+id.get(i)+" and name = '"+InvestmentDeptName.replace("_", " ")+"'");

                }

            }
            if(cal.getTime().equals(lastDate)) {
                if(balance >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balance+"").replace("-", "")+"', toGet='' where name='"+InvestmentDeptName.replace("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balance+"").replace("-", "")+"', toGive='' where name='"+InvestmentDeptName.replace("_", " ")+"'");

            }
        }
    }
    
    private void insertDayBook(int id, String date, String category, String toGet, String toGive) throws Exception {
        if(!date.equals("")) {
            if(!toGet.equals("")) {
                if(!toGet.equals("0")) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+InvestmentDeptName.replaceAll("_", " ")+"',"
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
                                + "'"+InvestmentDeptName.replaceAll("_", " ")+"',"
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
    
    public String getLastBalance(String date) throws SQLException {
        int id = getLastId(date);
        rs = stmt.executeQuery(" SELECT * FROM "+InvestmentDeptName+" WHERE ID = "+id );
        while(rs.next()) {
           return rs.getString("Balance");
        }
        return "0";
    }
    public int getLastId(String date) throws SQLException {
        int id = 1;
        rs = stmt.executeQuery("select * from "+InvestmentDeptName+" where date <= '"+date+"' order by id desc limit 1");
        while(rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }
    public int getNextId(String id) throws SQLException {
        rs = stmt.executeQuery("select id from "+InvestmentDeptName+" where id > '"+id+"' limit 1");
        while(rs.next()) {
            return rs.getInt("id");
        }
        return Integer.parseInt(id);
    }
    
    public int getMaxId() throws SQLException {
        rs = stmt.executeQuery("select max(id) as lstid from "+InvestmentDeptName+"");
        while(rs.next()) {
            return rs.getInt("lstid");
        }
        return 0;
    }
    
    public void deleteInvestmentDeptData(String date, String id) throws Exception {
        stmt.executeUpdate("Delete from "+InvestmentDeptName+" where id = '"+id+"'");
        stmt.executeUpdate("Delete from AllInvestmentDeptDetails where InvestmentDeptId = '"+id+"'");
        int oldBal = Integer.parseInt(getLastBalance(date));
        if(oldBal >=0)
            stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(oldBal+"").replace("-", "")+"', toGet='' where name='"+InvestmentDeptName.replace("_", " ")+"'");
        else
            stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(oldBal+"").replace("-", "")+"', toGive='' where name='"+InvestmentDeptName.replace("_", " ")+"'");
        updateDB(date, oldBal+"");
    }
    
    public void close() throws SQLException {
        connection.close();
    }
    
    public Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ObservableList list;
    private String InvestmentDeptName="";

}
