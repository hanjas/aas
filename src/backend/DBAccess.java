/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.Paper;
import model.tableModel.InsertOtherDetailsTableModel;

/**
 *
 * @author Haxx
 */
public class DBAccess {
    
    public DBAccess() throws Exception {
        
//        System.out.println("height - width "+(Paper.A4.getHeight()/2));
//        System.out.println("width - height "+(Paper.A4.getWidth()));
                
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:datas.sqlite");
        stmt = connection.createStatement();
        list = FXCollections.observableArrayList();
        
//        dropTables();
//        editAll1stTime();     // this edit function is to edit the sellers tables to correct as fistletter something
//        editAll2ndTime();     // this edit function is to edit the sellers tables to correct as fistletter something
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Sellers ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "BillNo TEXT, "
                + "OpBalToGet TEXT, "
                + "OpBalToGive TEXT, "
                + "Comm TEXT, "
                + "CapitalAdvance TEXT, "
                + "DuplicateAdvance TEXT, "
                + "AdvCutting TEXT, "
                + "PercentCutting TEXT, "
                + "totalPercentCutt TEXT, "
                + "BoxExp TEXT, "
                + "Groupe TEXT, "
                + "BankName TEXT, "
                + "AcNo TEXT, "
                + "Branch TEXT, "
                + "Balance TEXT, "
                + "IfcCode TEXT"
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Buyers ("    // Buyers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "BillNo TEXT, "
                + "OpBalToGet TEXT, "
                + "OpBalToGive TEXT, "
                + "Groupe TEXT, "
                + "BankName TEXT, "
                + "AcNo TEXT, "
                + "Branch TEXT, "
                + "Balance TEXT, "
                + "IfcCode TEXT"
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Outstanding ("    // Outstanding Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "OpBalToGet TEXT, "
                + "OpBalToGive TEXT, "
                + "Balance TEXT, "
                + "lastId TEXT, "
                + "Groupe TEXT "
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS OutstanderDetails ("    // Outstanding Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "ToGet TEXT, "
                + "ToGive TEXT "
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Investment ("    // Outstanding Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "OpBalToGet TEXT, "
                + "OpBalToGive TEXT, "
                + "Balance TEXT, "
                + "lastId TEXT, "
                + "Groupe TEXT "
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS InvestmentDetails ("    // Outstanding Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "ToGet TEXT, "
                + "ToGive TEXT "
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS InvestmentDept ("    // Outstanding Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "OpBalToGet TEXT, "
                + "OpBalToGive TEXT, "
                + "Balance TEXT, "
                + "lastId TEXT, "
                + "Groupe TEXT "
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS InvestmentDeptDetails ("    // Outstanding Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique, "
                + "ToGet TEXT, "
                + "ToGive TEXT "
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Fish ("    // Fish Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique"
                + "); "
        );
        
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Groupe ("    // Groupe Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AllSellerDetails ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT , "
                + "BillNo TEXT , "
                + "LinerName TEXT , "
                + "TruckNo TEXT , "
                + "Date Date , "
                + "FishId TEXT, "
                + "Comm TEXT, "
                + "BoxExp TEXT, "
                + "TotalBox TEXT, "
                + "MarketExp TEXT, "
                + "AdvCutting TEXT, "
                + "PercentCutting TEXT, "
                + "Ice TEXT, "
                + "PartyIce TEXT, "
                + "TruckRent TEXT, "
                + "PartyTruckRent TEXT, "
                + "LinerAmt TEXT, "
                + "OtherExp TEXT, "
                + "PartyExp TEXT, "
                + "TotalBillAmt TEXT, "
                + "TotalExp TEXT, "
                + "SubTotal TEXT, "
                + "OldBal TEXT, "
                + "GrandTotal TEXT, "
                + "CashPaid TEXT, "
                + "Excess TEXT, "
                + "Loss TEXT, "
                + "Balance TEXT,"
                + "TotalBoxSold TEXT , "
                + "SaleAmt TEXT , "
                + "RemainingBox TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AllBuyerDetails ("    
                + "Id INTEGER PRIMARY KEY, "
                + "BuyerId TEXT , "
                + "SellerName TEXT , "
                + "Name TEXT , "
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
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AllOutstandingDetails ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "OutstanderID Text , "
                + "Name Text , "
                + "Date Date , "
                + "Category TEXT , "
                + "OldBal TEXT , "
                + "ToGet TEXT, "
                + "ToGive TEXT, "
                + "Balance TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AllInvestmentDetails ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "InvestmentID Text , "
                + "Name Text , "
                + "Date Date , "
                + "Category TEXT , "
                + "OldBal TEXT , "
                + "ToGet TEXT, "
                + "ToGive TEXT, "
                + "Balance TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AllInvestmentDeptDetails ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "InvestmentDeptID Text , "
                + "Name Text , "
                + "Date Date , "
                + "Category TEXT , "
                + "OldBal TEXT , "
                + "ToGet TEXT, "
                + "ToGive TEXT, "
                + "Balance TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS fish_details ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT , "
                + "BillNo TEXT , "
                + "Particular TEXT , "
                + "Box TEXT , "
                + "Kg TEXT , "
                + "Rate TEXT, "
                + "Rs TEXT "
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DayBook ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "idd Text , "
                + "Name Text , "
                + "Date Date , "
                + "Category TEXT , "
                + "ToGet TEXT , "
                + "Expence TEXT, "
                + "NonExpence TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS IncomeDetails ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Category TEXT , "
                + "Groupe TEXT , "
                + "Date Date , "
                + "Amount TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ExpenceDetails ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Category TEXT , "
                + "Groupe TEXT , "
                + "Date Date , "
                + "Amount TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS BalanceSheet ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Date Date , "
                + "Name TEXT unique , "
                + "Category TEXT , "
                + "Priority TEXT , "
                + "ToGet TEXT , "
                + "ToGive TEXT"
                + "); "
        );
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS FinalBalanceSheet ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
                + "Name TEXT unique , "
                + "Priority TEXT , "
                + "ToGet TEXT , "
                + "ToGive TEXT"
                + "); "
        );
    }
    
    public void createSeller(String name, String opToGet, String opToGive, String comm,String capitalAdvance,String duplicateAdvance, String advCutting, String percentCutting, String totalPercentCutting, String boxExp, String group, String bankName, String acNo, String branch, String ifcCode) throws SQLException {
        stmt.executeUpdate("Insert into Sellers ("
                
                + "Name, OpBalToGet, OpBalToGive, Comm, CapitalAdvance,"
                + "DuplicateAdvance, AdvCutting, PercentCutting, totalPercentCutt, BoxExp, Groupe, "
                + "BankName, AcNo, Branch, IfcCode"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+opToGet+"',"
                + "'"+opToGive+"',"
                + "'"+comm+"',"
                + "'"+capitalAdvance+"',"
                + "'"+duplicateAdvance+"',"
                + "'"+advCutting+"',"
                + "'"+percentCutting+"',"
                + "'"+totalPercentCutting+"',"
                + "'"+boxExp+"',"
                + "'"+group+"',"
                + "'"+bankName+"',"
                + "'"+acNo+"',"
                + "'"+branch+"',"
                + "'"+ifcCode+"'"
                + "); "
                + "");
    }
    public void createBuyers(String name, String opToGet, String opToGive, String group, String bankName, String acNo, String branch, String ifcCode) throws SQLException {
        stmt.executeUpdate("Insert into Buyers ("
                
                + "Name, OpBalToGet, OpBalToGive,Groupe, "
                + "BankName, AcNo, Branch, IfcCode"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+opToGet+"',"
                + "'"+opToGive+"',"
                + "'"+group+"',"
                + "'"+bankName+"',"
                + "'"+acNo+"',"
                + "'"+branch+"',"
                + "'"+ifcCode+"'"
                + "); "
                + "");
    }
    public void createOutstanding(String name, String opToGet, String opToGive, String group) throws SQLException {
        stmt.executeUpdate("Insert into Outstanding ("
                
                + "Name, OpBalToGet, OpBalToGive, Groupe"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+opToGet+"',"
                + "'"+opToGive+"',"
                + "'"+group+"'"
                + "); "
                + "");
    }
    public void createInvestment(String name, String opToGet, String opToGive, String group) throws SQLException {
        stmt.executeUpdate("Insert into Investment ("
                
                + "Name, OpBalToGet, OpBalToGive, Groupe"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+opToGet+"',"
                + "'"+opToGive+"',"
                + "'"+group+"'"
                + "); "
                + "");
    }
    public void createInvestmentDept(String name, String opToGet, String opToGive, String group) throws SQLException {
        stmt.executeUpdate("Insert into InvestmentDept ("
                
                + "Name, OpBalToGet, OpBalToGive, Groupe"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+opToGet+"',"
                + "'"+opToGive+"',"
                + "'"+group+"'"
                + "); "
                + "");
    }
    public void createGroupe(String name) throws SQLException {
        stmt.executeUpdate("Insert into Groupe (Name) Values ('"+name+"') ");
    }
    public void createFish(String name) throws SQLException {
        stmt.executeUpdate("Insert into Fish (Name) Values ('"+name+"') ");
    }
    public void createNameInBalanceSheet(String date, String name, String category, String priority, String toGet, String toGive) throws SQLException {
        stmt.executeUpdate("Insert into BalanceSheet ("
            + "Date, Name, Category, Priority, ToGet, ToGive "
            + ") Values "
            + "("
            + "'"+date+"',"
            + "'"+name+"',"
            + "'"+category+"',"
            + "'"+priority+"',"
            + "'"+toGet+"',"
            + "'"+toGive+"'"
            + "); "
        );
    }
    
    public void insertOutstanderDetails(String name, String toGet, String toGive) throws SQLException {
        stmt.executeUpdate("Insert into OutstanderDetails ("
                
                + "Name, ToGet, ToGive"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+toGet+"',"
                + "'"+toGive+"'"
                + "); "
                + "");
    }
    public void insertInvestmentDetails(String name, String toGet, String toGive) throws SQLException {
        stmt.executeUpdate("Insert into InvestmentDetails ("
                
                + "Name, ToGet, ToGive"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+toGet+"',"
                + "'"+toGive+"'"
                + "); "
                + "");
    }
    public void insertInvestmentDeptDetails(String name, String toGet, String toGive) throws SQLException {
        stmt.executeUpdate("Insert into InvestmentDeptDetails ("
                
                + "Name, ToGet, ToGive"
                
                + ") Values "
                + "("
                + "'"+name+"',"
                + "'"+toGet+"',"
                + "'"+toGive+"'"
                + "); "
                + "");
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
            if(!rs.getString("lastDate").equals("") && rs.getString("lastDate") != null)
                lastDate = df.parse(rs.getString("lastDate"));
        }
        if(lastDate != null) {
            while(cal.getTime().before(lastDate) || cal.getTime().equals(today) && !lastDate.equals(null)) {
                cal.add( Calendar.DATE, 1 );
                String nextDate = dateFormat.format(cal.getTime());
                int check =0, balance = 0;
                rs = stmt.executeQuery("select * from DayBook where date = '"+date+"'");
                while(rs.next()){
                    balance = balance + rs.getInt("ToGet") - rs.getInt("expence") - rs.getInt("nonExpence");
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
    }
    public void updateBalanceSheet() throws SQLException{
        int totalCapital=0, totalAdvCutt=0, totalPercentCutt=0, totalSellerGet=0, totalSellerGive=0,
                totalBuyerGet=0, totalBuyerGive=0, totalOutstandingGet=0, totalOutstandingGive=0,
                totalInvestmentGet=0, totalInvestmentGive=0, totalInvestmentDeptGet=0, totalInvestmentDeptGive=0;
        rs = stmt.executeQuery("select sum(capitalAdvance) as totalCapital, sum(DuplicateAdvance) as totalAdvCutt from Sellers");
        while(rs.next()){
            totalCapital = rs.getInt("totalCapital");
            totalAdvCutt = rs.getInt("totalAdvCutt");
        }
        rs = stmt.executeQuery("select sum(PercentCutting) as totalPercentCutt from AllSellerDetails");
        while(rs.next()){
            totalPercentCutt = rs.getInt("totalPercentCutt");
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        rs = stmt.executeQuery("select sum(toget) as get, sum(toGive) as give from BalanceSheet where Category='Seller'");
        while(rs.next()) {
            totalSellerGet= rs.getInt("get");
            totalSellerGive = rs.getInt("give");
        }
        rs = stmt.executeQuery("select sum(toget) as get, sum(toGive) as give from BalanceSheet where Category='Buyer'");
        while(rs.next()) {
            totalBuyerGet= rs.getInt("get");
            totalBuyerGive = rs.getInt("give");
        }
        rs = stmt.executeQuery("select sum(toget) as get, sum(toGive) as give from BalanceSheet where Category='Outstanding'");
        while(rs.next()) {
            totalOutstandingGet= rs.getInt("get");
            totalOutstandingGive = rs.getInt("give");
        }
        rs = stmt.executeQuery("select sum(toget) as get, sum(toGive) as give from BalanceSheet where Category='Investment'");
        while(rs.next()) {
            totalInvestmentGet= rs.getInt("get");
            totalInvestmentGive = rs.getInt("give");
        }
        rs = stmt.executeQuery("select sum(toget) as get, sum(toGive) as give from BalanceSheet where Category='InvestmentDept'");
        while(rs.next()) {
            totalInvestmentDeptGet= rs.getInt("get");
            totalInvestmentDeptGive = rs.getInt("give");
        }
        
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'InvestmentGiven',"
                + "'1',"
                + "'"+totalInvestmentGet+"',"
                + "'"+totalInvestmentGive+"'"
                + ")"
                + "");
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'InvestmentTaken',"
                + "'2',"
                + "'"+totalInvestmentDeptGet+"',"
                + "'"+totalInvestmentDeptGive+"'"
                + ")"
                + "");
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'Outstanding',"
                + "'3',"
                + "'"+totalOutstandingGet+"',"
                + "'"+totalOutstandingGive+"'"
                + ")"
                + "");
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'Buyer',"
                + "'4',"
                + "'"+totalBuyerGet+"',"
                + "'"+totalBuyerGive+"'"
                + ")"
                + "");
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'Seller',"
                + "'5',"
                + "'"+totalSellerGet+"',"
                + "'"+totalSellerGive+"'"
                + ")"
                + "");
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'CapitalAdvance',"
                + "'6',"
                + "'"+totalCapital+"',"
                + "''"
                + ")"
                + "");
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'AdvanceCutt',"
                + "'7',"
                + "'',"
                + "'"+totalAdvCutt+"'"
                + ")"
                + "");
        stmt.executeUpdate("insert or replace into FinalBalanceSheet ("
                + "Name, Priority, ToGet, ToGive"
                + ") Values "
                + "("
                + "'PercentCutt',"
                + "'8',"
                + "'',"
                + "'"+totalPercentCutt+"'"
                + ")"
                + "");
        
    }
    
    public ObservableList getSellersNames() throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from Sellers");
        while(rs.next()) {
            list.add(rs.getString("Name"));
        }
        return list;
    }
    public ObservableList getBuyersNames() throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from Buyers");
        while(rs.next()) {
            list.add(rs.getString("Name"));
        }
        return list;
    }
    public ObservableList getOutstandingsNames() throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from Outstanding");
        while(rs.next()) {
            list.add(rs.getString("Name"));
        }
        return list;
    }
    public ObservableList getInvestmentNames() throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from Investment");
        while(rs.next()) {
            list.add(rs.getString("Name"));
        }
        return list;
    }
    public ObservableList getInvestmentDeptNames() throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from InvestmentDept");
        while(rs.next()) {
            list.add(rs.getString("Name"));
        }
        return list;
    }
    public ObservableList getFishesNames() throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from Fish");
        while(rs.next()) {
            list.add(rs.getString("Name"));
        }
        return list;
    }
    public ObservableList getGroupesNames() throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from Groupe");
        while(rs.next()) {
            list.add(rs.getString("name"));
        }
        return list;
    }
    
    public ResultSet getSellersDetails(String name) throws SQLException {
        return stmt.executeQuery("Select * from Sellers where name = '"+name+"'");
    }
    public ResultSet getBuyerDetails(String name) throws SQLException {
        return stmt.executeQuery("Select * from Buyers where name = '"+name+"'");
    }
    public ResultSet getOutstandingDetails(String name) throws Exception {
        return stmt.executeQuery("Select * from Outstanding where name = '"+name+"'");
    }
    public ResultSet getInvestmentDetails(String name) throws Exception {
        return stmt.executeQuery("Select * from Investment where name = '"+name+"'");
    }
    public ResultSet getInvestmentDeptDetails(String name) throws Exception {
        return stmt.executeQuery("Select * from InvestmentDept where name = '"+name+"'");
    }
    
    public boolean sellerInserted(String name) throws SQLException {
        rs = stmt.executeQuery("select * from "+name.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT")+" where id >="+2);
        while(rs.next()) {
            return true;
        }
        return false;
    }
    public boolean buyerInserted(String name) throws SQLException {
        rs = stmt.executeQuery("select * from "+name.replaceAll("\\s+", "_")+" where id >="+2);
        while(rs.next()) {
            return true;
        }
        return false;
    }
    public boolean outstanderInserted(String name) throws SQLException {
        rs = stmt.executeQuery("select * from "+name.replaceAll("\\s+", "_")+" where id >="+2);
        while(rs.next()) {
            return true;
        }
        return false;
    }
    public boolean InvestmentInserted(String name) throws SQLException {
        rs = stmt.executeQuery("select * from "+name.replaceAll("\\s+", "_")+" where id >="+2);
        while(rs.next()) {
            return true;
        }
        return false;
    }
    public boolean InvestmentDeptInserted(String name) throws SQLException {
        rs = stmt.executeQuery("select * from "+name.replaceAll("\\s+", "_")+" where id >="+2);
        while(rs.next()) {
            return true;
        }
        return false;
    }
    
    public void deleteSeller(String name) throws SQLException {
        stmt.executeUpdate("delete from Sellers where name='"+name+"'");
        stmt.executeUpdate("delete from BalanceSheet where name='"+name+"'");
        stmt.executeUpdate("delete from AllSellerDetails where name='"+name+"'");
        stmt.executeUpdate("delete from DayBook where name='"+name+"'");
        stmt.executeUpdate("drop table FIRSTLETTER"+name.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT"));
        stmt.executeUpdate("drop table FIRSTLETTER"+name.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT")+"_fish_details");
    }
    public void deleteBuyer(String name) throws SQLException {
        stmt.executeUpdate("delete from Buyers where name='"+name+"'");
        stmt.executeUpdate("delete from BalanceSheet where name='"+name+"'");
        stmt.executeUpdate("delete from AllBuyerDetails where name='"+name+"'");
        stmt.executeUpdate("drop table "+name.replaceAll("\\s+", "_"));
        stmt.executeUpdate("delete from DayBook where name='"+name+"'");
    }
    public void deleteOutstanding(String name) throws SQLException {
        stmt.executeUpdate("delete from Outstanding where name='"+name+"'");
        stmt.executeUpdate("delete from OutstanderDetails where name='"+name+"'");
        stmt.executeUpdate("delete from BalanceSheet where name='"+name+"'");
        stmt.executeUpdate("delete from AllOutstandingDetails where name='"+name+"'");
        stmt.executeUpdate("delete from DayBook where name='"+name+"'");
        stmt.executeUpdate("drop table "+name.replaceAll("\\s+", "_"));
    }
    public void deleteInvestment(String name) throws SQLException {
        stmt.executeUpdate("delete from Investment where name='"+name+"'");
        stmt.executeUpdate("delete from InvestmentDetails where name='"+name+"'");
        stmt.executeUpdate("delete from BalanceSheet where name='"+name+"'");
        stmt.executeUpdate("delete from AllInvestmentDetails where name='"+name+"'");
        stmt.executeUpdate("delete from DayBook where name='"+name+"'");
        stmt.executeUpdate("drop table "+name.replaceAll("\\s+", "_"));
    }
    public void deleteInvestmentDept(String name) throws SQLException {
        stmt.executeUpdate("delete from InvestmentDept where name='"+name+"'");
        stmt.executeUpdate("delete from InvestmentDeptDetails where name='"+name+"'");
        stmt.executeUpdate("delete from BalanceSheet where name='"+name+"'");
        stmt.executeUpdate("delete from AllInvestmentDeptDetails where name='"+name+"'");
        stmt.executeUpdate("delete from DayBook where name='"+name+"'");
        stmt.executeUpdate("drop table "+name.replaceAll("\\s+", "_"));
    }
    
    
    public ObservableList getSalesDetails(String sellerName, String billNo) throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select buyerId,name, boxcount, billAmt, cashPaid from AllBuyerDetails where SellerName = '"+sellerName+"' and billNo= '"+billNo+"'");
        while(rs.next()) {
            list.add(new InsertOtherDetailsTableModel(
                    rs.getString("name"),
                    rs.getString("boxcount"),
                    rs.getString("billAmt"),
                    rs.getString("cashPaid"),
                    rs.getString("buyerId")
            ));
        }
        return list;
    }
    
    public ResultSet getOutstandingDetails(String name, String fromDate, String toDate) throws SQLException {
        return stmt.executeQuery("select * from DayBook where name = '"+name+"' and date between '"+fromDate+"' and '"+toDate+"'");
    }
    public ResultSet getInvestmentDetails(String name, String fromDate, String toDate) throws SQLException {
        return stmt.executeQuery("select * from DayBook where name = '"+name+"' and date between '"+fromDate+"' and '"+toDate+"'");
    }
    public ResultSet getInvestmentDeptDetails(String name, String fromDate, String toDate) throws SQLException {
        return stmt.executeQuery("select * from DayBook where name = '"+name+"' and date between '"+fromDate+"' and '"+toDate+"'");
    }
    public ResultSet getIncomeDetails(String fromDate, String toDate) throws SQLException {
        return stmt.executeQuery("select * from DayBook where Category = 'Direct Income' and date between '"+fromDate+"' and '"+toDate+"'");
    }
    public ResultSet getExpenceDetails(String fromDate, String toDate) throws SQLException {
        return stmt.executeQuery("select * from DayBook where Category = 'Direct Expence' and date between '"+fromDate+"' and '"+toDate+"'");
    }
    public ResultSet getBills(String date) throws SQLException {
        return stmt.executeQuery("select * from AllSellerDetails where date='"+date+"'");
    }
    
    
    public void deleteDayBook(String id) throws SQLException {
        stmt.executeUpdate("delete from DayBook where id='"+id+"'");
    }
    public void deleteIncome(String id) throws SQLException {
        stmt.executeUpdate("delete from IncomeDetails where id='"+id+"'");
    }
    public void deleteExpence(String id) throws SQLException {
        stmt.executeUpdate("delete from ExpenceDetails where id='"+id+"'");
    }
    
    public void editSeller(String oldName, String name,String toGet,String toGive,String comm
            ,String percentCutt, String advCutt, String boxExp,String group,String bankName,String acno,
            String branch,String ifcCode) throws Exception {
        stmt.executeUpdate("UPDATE Sellers set "
                + "name='"+name+"',"
                + "Opbaltoget='"+toGet+"',"
                + "opbaltogive='"+toGive+"',"
                + "comm='"+comm+"',"
                + "percentCutting='"+percentCutt+"',"
                + "advCutting='"+advCutt+"',"
                + "BoxExp='"+boxExp+"',"
                + "groupe='"+group+"',"
                + "bankName='"+bankName+"',"
                + "acno='"+acno+"',"
                + "branch='"+branch+"',"
                + "ifcCode='"+ifcCode+"'"
                + " where name='"+oldName+"'");
        stmt.executeUpdate("update AllSellerDetails set name='"+name+"' where name='"+oldName+"'");
        stmt.executeUpdate("update AllBuyerDetails set SellerName='"+name+"' where SellerName='"+oldName+"'");
        stmt.executeUpdate("update DayBook set Name='"+name+"' where Name='"+oldName+"'");
        stmt.executeUpdate("update BalanceSheet set Name='"+name+"', toGive='"+toGive+"', toGet='"+toGet+"'  where Name='"+oldName+"'");
        String newName = "FIRSTLETTER"+name.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
        String oldName2 = "FIRSTLETTER"+oldName.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
        if(!oldName.equals(name)) {
            stmt.executeUpdate("Alter Table "+oldName2+" Rename To "+newName);
            stmt.executeUpdate("Alter Table "+oldName2+"_fish_details Rename To "+newName+"_fish_details");
//            stmt.executeUpdate("Alter Table "+oldName.replaceAll("\\s+", "_")+" Rename To "+newName);
//            stmt.executeUpdate("Alter Table "+oldName.replaceAll("\\s+", "_")+"_fish_details Rename To "+newName+"_fish_details");
        }
        String names = "FIRSTLETTER"+name.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
        if(toGet.equals(""))
            correctSellerOpBal(names, toGive);
        if(toGive.equals(""))
            correctSellerOpBal(names, "-"+toGet);
    }
    public void editBuyer(String oldName, String name, String toGet,
            String toGive, String group, String bankName, String acno,
            String branch, String ifcCode) throws Exception {
        stmt.executeUpdate("UPDATE Buyers set "
                + "Name='"+name+"',"
                + "OpBalToGet='"+toGet+"',"
                + "OpBalToGive='"+toGive+"',"
                + "groupe='"+group+"',"
                + "bankName='"+bankName+"',"
                + "acno='"+acno+"',"
                + "branch='"+branch+"',"
                + "ifcCode='"+ifcCode+"'"
                + " where name='"+oldName+"'");
        stmt.executeUpdate("update AllBuyerDetails set name='"+name+"' where name='"+oldName+"'");
        stmt.executeUpdate("update DayBook set Name='"+name+"' where Name='"+oldName+"'");
        stmt.executeUpdate("update BalanceSheet set Name='"+name+"', toGive='"+toGet+"', toGet='"+toGive+"'    where Name='"+oldName+"'");
        if(!oldName.equals(name))
            stmt.executeUpdate("Alter Table "+oldName.replaceAll("\\s+", "_")+" Rename To "+name.replaceAll("\\s+", "_"));
        if(toGet.equals(""))
            correctBuyerOpBal(name.replaceAll("\\s+", "_"), toGive);
        if(toGive.equals(""))
            correctBuyerOpBal(name.replaceAll("\\s+", "_"), "-"+toGet);
    }
    public void editOutstander(String oldName, String name, String toGet, String toGive, String group) throws Exception {
        stmt.executeUpdate("UPDATE Outstanding set "
                + "Name='"+name+"',"
                + "OpBalToGet='"+toGet+"',"
                + "OpBalToGive='"+toGive+"',"
                + "groupe='"+group+"'"
                + " where name='"+oldName+"'");
        stmt.executeUpdate("update AllOutstandingDetails set name='"+name+"' where name='"+oldName+"'");
        stmt.executeUpdate("update DayBook set Name='"+name+"' where Name='"+oldName+"'");
        stmt.executeUpdate("update BalanceSheet set Name='"+name+"', toGive='"+toGive+"', toGet='"+toGet+"'  where Name='"+oldName+"'");
        if(!oldName.equals(name))
            stmt.executeUpdate("Alter Table "+oldName.replaceAll("\\s+", "_")+" Rename To "+name.replaceAll("\\s+", "_"));
        if(toGet.equals(""))
            correctOutstandingOpBal(name.replaceAll("\\s+", "_"), toGive);
        if(toGive.equals(""))
            correctOutstandingOpBal(name.replaceAll("\\s+", "_"), "-"+toGet);
    }
    public void editInvestment(String oldName, String name, String toGet, String toGive, String group) throws Exception {
        stmt.executeUpdate("UPDATE Investment set "
                + "Name='"+name+"',"
                + "OpBalToGet='"+toGet+"',"
                + "OpBalToGive='"+toGive+"',"
                + "groupe='"+group+"'"
                + " where name='"+oldName+"'");
        stmt.executeUpdate("update AllInvestmentDetails set name='"+name+"' where name='"+oldName+"'");
        stmt.executeUpdate("update DayBook set Name='"+name+"' where Name='"+oldName+"'");
        stmt.executeUpdate("update BalanceSheet set Name='"+name+"', toGive='"+toGive+"', toGet='"+toGet+"'  where Name='"+oldName+"'");
        if(!oldName.equals(name))
            stmt.executeUpdate("Alter Table "+oldName.replaceAll("\\s+", "_")+" Rename To "+name.replaceAll("\\s+", "_"));
        if(toGet.equals(""))
            correctOutstandingOpBal(name.replaceAll("\\s+", "_"), toGive);
        if(toGive.equals(""))
            correctOutstandingOpBal(name.replaceAll("\\s+", "_"), "-"+toGet);
    }
    public void editInvestmentDept(String oldName, String name, String toGet, String toGive, String group) throws Exception {
        stmt.executeUpdate("UPDATE InvestmentDept set "
                + "Name='"+name+"',"
                + "OpBalToGet='"+toGet+"',"
                + "OpBalToGive='"+toGive+"',"
                + "groupe='"+group+"'"
                + " where name='"+oldName+"'");
        stmt.executeUpdate("update AllInvestmentDeptDetails set name='"+name+"' where name='"+oldName+"'");
        stmt.executeUpdate("update DayBook set Name='"+name+"' where Name='"+oldName+"'");
        stmt.executeUpdate("update BalanceSheet set Name='"+name+"', toGive='"+toGive+"', toGet='"+toGet+"'  where Name='"+oldName+"'");
        if(!oldName.equals(name))
            stmt.executeUpdate("Alter Table "+oldName.replaceAll("\\s+", "_")+" Rename To "+name.replaceAll("\\s+", "_"));
        if(toGet.equals(""))
            correctOutstandingOpBal(name.replaceAll("\\s+", "_"), toGive);
        if(toGive.equals(""))
            correctOutstandingOpBal(name.replaceAll("\\s+", "_"), "-"+toGet);
    }
    public void editGroup(String oldGroupe, String newGroupe) throws SQLException {
        stmt.executeUpdate("update Sellers set groupe='"+newGroupe+"' where groupe='"+oldGroupe+"'");
        stmt.executeUpdate("update Buyers set groupe='"+newGroupe+"' where groupe='"+oldGroupe+"'");
        stmt.executeUpdate("update Outstanding set groupe='"+newGroupe+"' where groupe='"+oldGroupe+"'");
        stmt.executeUpdate("update Groupe set name='"+newGroupe+"' where name='"+oldGroupe+"'");
    }
    public void editFish(String oldName, String newName) throws SQLException {
        stmt.executeUpdate("update Fish set name='"+newName+"' where name='"+oldName+"'");
    }
    
    public void correctSellerOpBal(String name, String opBal) throws Exception {
        stmt.executeUpdate("update "+name+" set balance='"+opBal+"' where id=1");
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        java.util.Date lastDate=null;
        String nextDate="";
        int oldBal=0, subTotal=0, grandTotal=0, balance=0, check=0;
        
        List<String> oldBalarray =new ArrayList<String>();
        List<String> subTotalArray =new ArrayList<String>();
        List<String> grandTotalArray =new ArrayList<String>();
        List<String> balanceArray =new ArrayList<String>();
        List<String> id =new ArrayList<String>();
        
        balance = Integer.parseInt(opBal);
        Calendar nowDate = Calendar.getInstance();
        rs = stmt.executeQuery("select * from "+name+" where id = 2");
        while(rs.next()) {
            nowDate.setTime( dateFormat.parse( rs.getString("date") ) );
            check=1;
        }
        if(check==1) {
            rs = stmt.executeQuery("select max(date) as lastDate from "+name);
            while(rs.next()) {
                lastDate = dateFormat.parse(rs.getString("lastDate"));
            }
            while(nowDate.getTime().before(lastDate) || nowDate.getTime().equals(lastDate)){
                nextDate = dateFormat.format(nowDate.getTime());
                rs = stmt.executeQuery("select * from "+name+" where date='"+nextDate+"' order By id");
                while(rs.next()) {
                    oldBal = balance;
                    subTotal = rs.getInt("TotalBillAmt") - rs.getInt("TotalExp");
                    grandTotal = subTotal + oldBal;
                    balance = grandTotal - rs.getInt("CashPaid");
                    id.add(rs.getString("id"));
                    oldBalarray.add(oldBal+"");
                    subTotalArray.add(subTotal+"");
                    grandTotalArray.add(grandTotal+"");
                    balanceArray.add(balance+"");
                }
                for(int i =0; i< id.size(); i++){
                    stmt.executeUpdate("update "+name+" set OldBal='"+oldBalarray.get(i)+"',"
                            + "SubTotal='"+subTotalArray.get(i)+"',"
                            + "GrandTotal='"+grandTotalArray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where id="+id.get(i));
                    stmt.executeUpdate("update AllSellerDetails set OldBal='"+oldBalarray.get(i)+"',"
                            + "SubTotal='"+subTotalArray.get(i)+"',"
                            + "GrandTotal='"+grandTotalArray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where BillNo="+id.get(i)+" and name='"+name.replaceAll("_", " ")+"'");
                    stmt.executeUpdate("UPDATE Sellers SET  balance = '"+balanceArray.get(i)+"' where name = '"+name.replace("_", " ")+"'");
                    if(Integer.parseInt(balanceArray.get(i)) >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balanceArray.get(i)+"").replace("-", "")+"', toGet='' where name='"+name.replaceAll("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balanceArray.get(i)+"").replace("-", "")+"', toGive='' where name='"+name.replaceAll("_", " ")+"'");

                }
                nowDate.add( Calendar.DATE, 1 );
            }
        }
    }
    public void correctBuyerOpBal(String name, String opBal) throws Exception {
        stmt.executeUpdate("update "+name+" set balance='"+opBal+"' where id=1");
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        java.util.Date lastDate=null;
        String nextDate="";
        int oldBal=0, total=0, balance=0, check=0;
        
        List<String> oldBalarray =new ArrayList<String>();
        List<String> totalArray =new ArrayList<String>();
        List<String> balanceArray =new ArrayList<String>();
        List<String> id =new ArrayList<String>();
        
        balance = Integer.parseInt(opBal);
        Calendar nowDate = Calendar.getInstance();
        rs = stmt.executeQuery("select * from "+name+" where id = 2");
        while(rs.next()) {
            nowDate.setTime( dateFormat.parse( rs.getString("date") ) );
            check=1;
        }
        if(check==1) {
            rs = stmt.executeQuery("select max(date) as lastDate from "+name);
            while(rs.next()) {
                lastDate = dateFormat.parse(rs.getString("lastDate"));
            }
            while(nowDate.getTime().before(lastDate) || nowDate.getTime().equals(lastDate)){
                nextDate = dateFormat.format(nowDate.getTime());
                rs = stmt.executeQuery("select * from "+name+" where date='"+nextDate+"' order By id");
                while(rs.next()) {
                    oldBal = balance;
                    total = oldBal+ rs.getInt("BillAmt");
                    balance = total - rs.getInt("CashPaid");
                    id.add(rs.getString("id"));
                    oldBalarray.add(oldBal+"");
                    totalArray.add(total+"");
                    balanceArray.add(balance+"");
                }
                for(int i =0; i< id.size(); i++){
                    stmt.executeUpdate("update "+name+" set OldBal='"+oldBalarray.get(i)+"',"
                            + "Total='"+totalArray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where id="+id.get(i));
                    stmt.executeUpdate("update AllBuyerDetails set OldBal='"+oldBalarray.get(i)+"',"
                            + "Total='"+totalArray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where BuyerId="+id.get(i)+" and name='"+name.replaceAll("_", " ")+"'");
                    stmt.executeUpdate("UPDATE Buyers SET  balance = '"+balanceArray.get(i)+"' where name = '"+name.replace("_", " ")+"'");
                    if(Integer.parseInt(balanceArray.get(i)) >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balanceArray.get(i)+"").replace("-", "")+"', toGet='' where name='"+name.replaceAll("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balanceArray.get(i)+"").replace("-", "")+"', toGive='' where name='"+name.replaceAll("_", " ")+"'");

                }
                nowDate.add( Calendar.DATE, 1 );
            }
        }
    }
    public void correctOutstandingOpBal(String name, String opBal) throws Exception {
        stmt.executeUpdate("update "+name+" set balance='"+opBal+"' where id=1");
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        java.util.Date lastDate=null;
        String nextDate="";
        int oldBal=0, balance=0, check=0;
        
        List<String> oldBalarray =new ArrayList<String>();
        List<String> balanceArray =new ArrayList<String>();
        List<String> id =new ArrayList<String>();
        
        balance = Integer.parseInt(opBal);
        Calendar nowDate = Calendar.getInstance();
        rs = stmt.executeQuery("select * from "+name+" where id = 2");
        while(rs.next()) {
            nowDate.setTime( dateFormat.parse( rs.getString("date") ) );
            check=1;
        }
        if(check==1) {
            rs = stmt.executeQuery("select max(date) as lastDate from "+name);
            while(rs.next()) {
                lastDate = dateFormat.parse(rs.getString("lastDate"));
            }
            while(nowDate.getTime().before(lastDate) || nowDate.getTime().equals(lastDate)){
                nextDate = dateFormat.format(nowDate.getTime());
                rs = stmt.executeQuery("select * from "+name+" where date='"+nextDate+"' order By id");
                while(rs.next()) {
                    oldBal = balance;
                    balance = oldBal + rs.getInt("ToGet") - rs.getInt("ToGive");
                    id.add(rs.getString("id"));
                    oldBalarray.add(oldBal+"");
                    balanceArray.add(balance+"");
                }
                for(int i =0; i< id.size(); i++){
                    stmt.executeUpdate("update "+name+" set OldBal='"+oldBalarray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where id="+id.get(i));
                    stmt.executeUpdate("update AllOutstandingDetails set OldBal='"+oldBalarray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where OutstanderID="+id.get(i)+" and name='"+name.replaceAll("_", " ")+"'");
                    stmt.executeUpdate("UPDATE Outstanding SET  balance = '"+balanceArray.get(i)+"' where name = '"+name.replace("_", " ")+"'");
                    if(Integer.parseInt(balanceArray.get(i)) >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balanceArray.get(i)+"").replace("-", "")+"', toGet='' where name='"+name.replaceAll("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balanceArray.get(i)+"").replace("-", "")+"', toGive='' where name='"+name.replaceAll("_", " ")+"'");

                }
                nowDate.add( Calendar.DATE, 1 );
            }
        }
    }
    public void correctInvestmentOpBal(String name, String opBal) throws Exception {
        stmt.executeUpdate("update "+name+" set balance='"+opBal+"' where id=1");
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        java.util.Date lastDate=null;
        String nextDate="";
        int oldBal=0, balance=0, check=0;
        
        List<String> oldBalarray =new ArrayList<String>();
        List<String> balanceArray =new ArrayList<String>();
        List<String> id =new ArrayList<String>();
        
        balance = Integer.parseInt(opBal);
        Calendar nowDate = Calendar.getInstance();
        rs = stmt.executeQuery("select * from "+name+" where id = 2");
        while(rs.next()) {
            nowDate.setTime( dateFormat.parse( rs.getString("date") ) );
            check=1;
        }
        if(check==1) {
            rs = stmt.executeQuery("select max(date) as lastDate from "+name);
            while(rs.next()) {
                lastDate = dateFormat.parse(rs.getString("lastDate"));
            }
            while(nowDate.getTime().before(lastDate) || nowDate.getTime().equals(lastDate)){
                nextDate = dateFormat.format(nowDate.getTime());
                rs = stmt.executeQuery("select * from "+name+" where date='"+nextDate+"' order By id");
                while(rs.next()) {
                    oldBal = balance;
                    balance = oldBal + rs.getInt("ToGet") - rs.getInt("ToGive");
                    id.add(rs.getString("id"));
                    oldBalarray.add(oldBal+"");
                    balanceArray.add(balance+"");
                }
                for(int i =0; i< id.size(); i++){
                    stmt.executeUpdate("update "+name+" set OldBal='"+oldBalarray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where id="+id.get(i));
                    stmt.executeUpdate("update AllInvestmentDetails set OldBal='"+oldBalarray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where InvestmentID="+id.get(i)+" and name='"+name.replaceAll("_", " ")+"'");
                    stmt.executeUpdate("UPDATE Investment SET  balance = '"+balanceArray.get(i)+"' where name = '"+name.replace("_", " ")+"'");
                    if(Integer.parseInt(balanceArray.get(i)) >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balanceArray.get(i)+"").replace("-", "")+"', toGet='' where name='"+name.replaceAll("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balanceArray.get(i)+"").replace("-", "")+"', toGive='' where name='"+name.replaceAll("_", " ")+"'");

                }
                nowDate.add( Calendar.DATE, 1 );
            }
        }
    }
    public void correctInvestmentDeptOpBal(String name, String opBal) throws Exception {
        stmt.executeUpdate("update "+name+" set balance='"+opBal+"' where id=1");
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        java.util.Date lastDate=null;
        String nextDate="";
        int oldBal=0, balance=0, check=0;
        
        List<String> oldBalarray =new ArrayList<String>();
        List<String> balanceArray =new ArrayList<String>();
        List<String> id =new ArrayList<String>();
        
        balance = Integer.parseInt(opBal);
        Calendar nowDate = Calendar.getInstance();
        rs = stmt.executeQuery("select * from "+name+" where id = 2");
        while(rs.next()) {
            nowDate.setTime( dateFormat.parse( rs.getString("date") ) );
            check=1;
        }
        if(check==1) {
            rs = stmt.executeQuery("select max(date) as lastDate from "+name);
            while(rs.next()) {
                lastDate = dateFormat.parse(rs.getString("lastDate"));
            }
            while(nowDate.getTime().before(lastDate) || nowDate.getTime().equals(lastDate)){
                nextDate = dateFormat.format(nowDate.getTime());
                rs = stmt.executeQuery("select * from "+name+" where date='"+nextDate+"' order By id");
                while(rs.next()) {
                    oldBal = balance;
                    balance = oldBal + rs.getInt("ToGet") - rs.getInt("ToGive");
                    id.add(rs.getString("id"));
                    oldBalarray.add(oldBal+"");
                    balanceArray.add(balance+"");
                }
                for(int i =0; i< id.size(); i++){
                    stmt.executeUpdate("update "+name+" set OldBal='"+oldBalarray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where id="+id.get(i));
                    stmt.executeUpdate("update AllInvestmentDeptDetails set OldBal='"+oldBalarray.get(i)+"',"
                            + "Balance='"+balanceArray.get(i)+"' "
                            + "where InvestmentDeptID="+id.get(i)+" and name='"+name.replaceAll("_", " ")+"'");
                    stmt.executeUpdate("UPDATE InvestmentDept SET  balance = '"+balanceArray.get(i)+"' where name = '"+name.replace("_", " ")+"'");
                    if(Integer.parseInt(balanceArray.get(i)) >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balanceArray.get(i)+"").replace("-", "")+"', toGet='' where name='"+name.replaceAll("_", " ")+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balanceArray.get(i)+"").replace("-", "")+"', toGive='' where name='"+name.replaceAll("_", " ")+"'");

                }
                nowDate.add( Calendar.DATE, 1 );
            }
        }
    }
    
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ObservableList list;
}
