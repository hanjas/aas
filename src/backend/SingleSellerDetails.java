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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.tableModel.BillTableModel;
import model.tableModel.EditBillTableModel;

/**
 *
 * @author BCz
 */
public class SingleSellerDetails {

    public SingleSellerDetails(String seller) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:datas.sqlite");
        stmt = connection.createStatement();
        list = FXCollections.observableArrayList();
        this.seller = seller;
        this.replaceSeller = seller.replaceAll("FIRSTLETTER", "").replaceAll("TMPDOT", "\\.").replaceAll("FORWARDSLASH", "/").replaceAll("_", " ");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+seller+" ("    // Sellers Table
                + "Id INTEGER PRIMARY KEY, "
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
                + "Balance TEXT"
                + "); "
        );
    }
    
    public void insertBillDetails(String linerName, String truckNo, String date, String fishId, String comm, String boxExp,String totalBox, String marketExp, String advCutting, String percentCutting,
                String ice, String partyIce, String truckRent, String partyTruckRent,  String linerAmt, String otherExp, String partyExp, String totalBillAmt, String totalExp, String subTotal, String oldBal, String grandTotal, String cashPaid,
                String balance
            ) throws Exception {
        stmt.executeUpdate("Insert into "+seller+" ("
                
                + "LinerName, truckNo, date, FishId, Comm, BoxExp, TotalBox, MarketExp, AdvCutting, PercentCutting, Ice, "
                + "PartyIce, TruckRent, PartyTruckRent, LinerAmt, OtherExp, PartyExp, TotalBillAmt, TotalExp, SubTotal, OldBal, GrandTotal, CashPaid, Balance "
                
                + ") Values "
                + "("
                + "'"+linerName+"',"
                + "'"+truckNo+"',"
                + "'"+date+"',"
                + "'"+fishId+"',"
                + "'"+comm+"',"
                + "'"+boxExp+"',"
                + "'"+totalBox+"',"
                + "'"+marketExp+"',"
                + "'"+advCutting+"',"
                + "'"+percentCutting+"',"
                + "'"+ice+"',"
                + "'"+partyIce+"',"
                + "'"+truckRent+"',"
                + "'"+partyTruckRent+"',"
                + "'"+linerAmt+"',"
                + "'"+otherExp+"',"
                + "'"+partyExp+"',"
                + "'"+totalBillAmt+"',"
                + "'"+totalExp+"',"
                + "'"+subTotal+"',"
                + "'"+oldBal+"',"
                + "'"+grandTotal+"',"
                + "'"+cashPaid+"',"
                + "'"+balance+"'"
                + "); "
                + "");
        stmt.executeUpdate("Insert into AllSellerDetails ("

                + "Name, BillNo, LinerName, truckNo, date, FishId, Comm, BoxExp, TotalBox, MarketExp, AdvCutting,"
                + " PercentCutting, Ice, PartyIce, TruckRent, PartyTruckRent, LinerAmt,  OtherExp, PartyExp, TotalBillAmt, TotalExp, SubTotal,"
                + " OldBal, GrandTotal, CashPaid, Balance, Loss, Excess, TotalBoxSold, SaleAmt, RemainingBox "

                + ") Values "
                + "("
                + "'"+replaceSeller+"',"
                + "'"+getMaxId()+"',"
                + "'"+linerName+"',"
                + "'"+truckNo+"',"
                + "'"+date+"',"
                + "'"+fishId+"',"
                + "'"+comm+"',"
                + "'"+boxExp+"',"
                + "'"+totalBox+"',"
                + "'"+marketExp+"',"
                + "'"+advCutting+"',"
                + "'"+percentCutting+"',"
                + "'"+ice+"',"
                + "'"+partyIce+"',"
                + "'"+truckRent+"',"
                + "'"+partyTruckRent+"',"
                + "'"+linerAmt+"',"
                + "'"+otherExp+"',"
                + "'"+partyExp+"',"
                + "'"+totalBillAmt+"',"
                + "'"+totalExp+"',"
                + "'"+subTotal+"',"
                + "'"+oldBal+"',"
                + "'"+grandTotal+"',"
                + "'"+cashPaid+"',"
                + "'"+balance+"',"
                + "'"+totalBillAmt+"',"
                + "'',"
                + "'',"
                + "'',"
                + "'"+totalBox+"'"
                + "); "
                + "");
        if(!date.equals("")){
            updateDB(date, balance);
        }
        insertDayBook(getMaxId(), date, cashPaid, partyTruckRent, linerAmt, partyExp, partyIce);
        if(Integer.parseInt(balance) > 0) 
            stmt.executeUpdate("update BalanceSheet set  toGive='"+balance+"'  where Name='"+replaceSeller+"'");
        else 
            stmt.executeUpdate("update BalanceSheet set  toGet='"+balance.replaceAll("-", "")+"'  where Name='"+replaceSeller+"'");
        stmt.executeUpdate("UPDATE Sellers SET billNo = '"+getLastBillNo()+"', balance = '"+balance+"' where name = '"+replaceSeller+"'");
    }
    
    private void updateDB(String fromDate, String todayBalance) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateFormat.parse( fromDate ) );
        java.util.Date lastDate=null;
        rs = stmt.executeQuery("select max(date) as lastDate from "+seller);
        while(rs.next()) {
            if(!rs.getString("lastDate").equals(""))
                lastDate = dateFormat.parse(rs.getString("lastDate"));
        }
        if(lastDate != null) {
            int balance = 0, grandTot=0;
            String nextDate = "";
            List<String> oldBal =new ArrayList<String>();
            List<String> grandTotal =new ArrayList<String>();
            List<String> id =new ArrayList<String>();
            List<String> bal =new ArrayList<String>();
            balance = Integer.parseInt(todayBalance);
            while(cal.getTime().before(lastDate) && !lastDate.equals(null)) {
                oldBal.clear();
                id.clear();
                grandTotal.clear();
                bal.clear();
                cal.add( Calendar.DATE, 1 );
                nextDate = dateFormat.format(cal.getTime());
                rs = stmt.executeQuery("select * from "+seller+" where date = '"+nextDate+"' order by id");
                while(rs.next()) {
                    grandTot = Integer.parseInt(todayBalance) + rs.getInt("subTotal");
                    balance = grandTot - rs.getInt("cashPaid");
                    oldBal.add(todayBalance);
                    grandTotal.add(grandTot+"");
                    bal.add(balance+"");
                    id.add(rs.getString("id"));
                    todayBalance = balance+"";
                }
                for(int i=0; i<oldBal.size(); i++) {
                    stmt.executeUpdate("update "+seller+" set oldBal='"+oldBal.get(i)+"', grandtotal='"+grandTotal.get(i)+"', balance='"+bal.get(i)+"' where id="+id.get(i));
                    stmt.executeUpdate("update AllSellerDetails set oldBal='"+oldBal.get(i)+"', grandtotal='"+grandTotal.get(i)+"', balance='"+bal.get(i)+"' where billNo='"+id.get(i)+"' and name = '"+replaceSeller+"'");
                }

            }
            if(cal.getTime().equals(lastDate)) {
                if(balance >=0)
                        stmt.executeUpdate("UPDATE BalanceSheet set toGive='"+(balance+"").replace("-", "")+"', toGet='' where name='"+replaceSeller+"'");
                    else
                        stmt.executeUpdate("UPDATE BalanceSheet set toGet='"+(balance+"").replace("-", "")+"', toGive='' where name='"+replaceSeller+"'");
                stmt.executeUpdate("UPDATE Sellers SET billNo = '"+getLastBillNo()+"', balance = '"+balance+"' where name = '"+replaceSeller+"'");
            }
        }
    }
    public void saveEditBill(String id, String linerName, String truckNo, String date, String fishId, String comm, String boxExp,String totalBox, String marketExp, String advCutting, String percentCutting,
                String ice, String partyIce, String truckRent, String partyTruckRent, String linerAmt, String otherExp, String partyExp, String totalBillAmt, String totalExp, String subTotal, String oldBal, String grandTotal, String cashPaid,
                String balance) throws SQLException, Exception{
        stmt.executeUpdate("update "+seller+" "
                
                + "set "
                
                + "LinerName='"+linerName+"',"
                + "truckNo='"+truckNo+"',"
                + "Comm='"+comm+"',"
                + "boxExp='"+boxExp+"',"
                + "TotalBox='"+totalBox+"',"
                + "MarketExp='"+marketExp+"',"
                + "AdvCutting='"+advCutting+"',"
                + "PercentCutting='"+percentCutting+"',"
                + "Ice='"+ice+"',"
                + "PartyIce='"+partyIce+"',"
                + "TruckRent='"+truckRent+"',"
                + "PartyTruckRent='"+partyTruckRent+"',"
                + "LinerAmt='"+linerAmt+"',"
                + "OtherExp='"+otherExp+"',"
                + "PartyExp='"+partyExp+"',"
                + "TotalBillAmt='"+totalBillAmt+"',"
                + "TotalExp='"+totalExp+"',"
                + "SubTotal='"+subTotal+"',"
                + "GrandTotal='"+grandTotal+"',"
                + "CashPaid='"+cashPaid+"',"
                + "Balance='"+balance+"'"
                
                + " where id="+id+" "
                + "");
        int sale=0,loss=0,excess=0,boxsold=0,profit=0;
        float remainingbox=0;
        rs= getBillDetails(replaceSeller, id);
        while(rs.next()) {
            sale = rs.getInt("saleAmt");
            boxsold = rs.getInt("totalBoxSold");
        }
        profit = sale - Integer.parseInt(totalBillAmt);
        if(profit>=0)
            excess = profit;
        else
            loss = profit;
        
        remainingbox = Float.parseFloat(totalBox) - boxsold;
        stmt.executeUpdate("Update AllSellerDetails "
                
                + "set "
                
                + "LinerName='"+linerName+"',"
                + "truckNo='"+truckNo+"',"
                + "Comm='"+comm+"',"
                + "boxExp='"+boxExp+"',"
                + "TotalBox='"+totalBox+"',"
                + "MarketExp='"+marketExp+"',"
                + "AdvCutting='"+advCutting+"',"
                + "PercentCutting='"+percentCutting+"',"
                + "Ice='"+ice+"',"
                + "PartyIce='"+partyIce+"',"
                + "TruckRent='"+truckRent+"',"
                + "PartyTruckRent='"+partyTruckRent+"',"
                + "LinerAmt='"+linerAmt+"',"
                + "OtherExp='"+otherExp+"',"
                + "PartyExp='"+partyExp+"',"
                + "TotalBillAmt='"+totalBillAmt+"',"
                + "TotalExp='"+totalExp+"',"
                + "SubTotal='"+subTotal+"',"
                + "GrandTotal='"+grandTotal+"',"
                + "CashPaid='"+cashPaid+"',"
                + "Balance='"+balance+"',"
                + "Loss='"+(loss+"").replaceAll("-", "")+"',"
                + "Excess='"+excess+"',"
                + "RemainingBox='"+remainingbox+"'"
                + "where name='"+replaceSeller+"' and billNo='"+id+"'"
                + ""
                + "");
        
        editDayBook(id, date, cashPaid, partyTruckRent, linerAmt, partyExp, partyIce);
        updateDB(date, balance);
    }
    private void editDayBook(String id, String date, String cashPaid, String partyTruckRent, String linerAmt, String partyExp, String partyIce) throws Exception {
        int checker=0;
        rs = stmt.executeQuery("select * from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Cash Paid'");
        while(rs.next()){
            checker=1;
        }
        if(checker==1){
            if(cashPaid.equals("") || cashPaid.equals("0"))
                stmt.executeUpdate("delete from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Cash Paid'");
            else
            stmt.executeUpdate("update DayBook set NonExpence='"+cashPaid+"' where name='"+replaceSeller+"' and idd='"+id+"' and category='Cash Paid'");
        } else {
            if(!cashPaid.equals("")) {
                if(Float.parseFloat(cashPaid)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Cash Paid',"
                                + "'',"
                                + "'',"
                                + "'"+cashPaid+"'"
                                + "); "
                                + ""
                        );
                }
            }
        }
        checker=0;
        rs = stmt.executeQuery("select * from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='PartyTruckRent'");
        while(rs.next()){
            checker=1;
        }
        if(checker==1){
            if(partyTruckRent.equals("") || partyTruckRent.equals("0"))
                stmt.executeUpdate("delete from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='PartyTruckRent'");
            else
            stmt.executeUpdate("update DayBook set NonExpence='"+partyTruckRent+"' where name='"+replaceSeller+"' and idd='"+id+"' and category='PartyTruckRent'");
        } else {
            if(!partyTruckRent.equals("")) {
                if(Float.parseFloat(partyTruckRent)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'PartyTruckRent',"
                                + "'',"
                                + "'',"
                                + "'"+partyTruckRent+"'"
                                + "); "
                                + ""
                        );
                }
            }
        }
        checker=0;
        rs = stmt.executeQuery("select * from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Liner Amt'");
        while(rs.next()){
            checker=1;
        }
        if(checker==1){
            if(linerAmt.equals("") || linerAmt.equals("0"))
                stmt.executeUpdate("delete from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Liner Amt'");
            else
            stmt.executeUpdate("update DayBook set NonExpence='"+linerAmt+"' where name='"+replaceSeller+"' and idd='"+id+"' and category='Liner Amt'");
        } else {
            if(!linerAmt.equals("")) {
                if(Float.parseFloat(linerAmt)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Liner Amt',"
                                + "'',"
                                + "'',"
                                + "'"+linerAmt+"'"
                                + "); "
                                + ""
                        );
                }
            }
        }
        checker=0;
        rs = stmt.executeQuery("select * from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Party Exp'");
        while(rs.next()){
            checker=1;
        }
        if(checker==1){
            if(partyExp.equals("") || partyExp.equals("0"))
                stmt.executeUpdate("delete from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Party Exp'");
            else
            stmt.executeUpdate("update DayBook set NonExpence='"+partyExp+"' where name='"+replaceSeller+"' and idd='"+id+"' and category='Party Exp'");
        } else {
            if(!partyExp.equals("")) {
                if(Float.parseFloat(partyExp)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Party Exp',"
                                + "'',"
                                + "'',"
                                + "'"+partyExp+"'"
                                + "); "
                                + ""
                        );
                }
            }
        }
        checker=0;
        rs = stmt.executeQuery("select * from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Party Ice'");
        while(rs.next()){
            checker=1;
        }
        if(checker==1){
            if(partyIce.equals("") || partyIce.equals("0"))
                stmt.executeUpdate("delete from DayBook where name='"+replaceSeller+"' and idd='"+id+"' and category='Party Ice'");
            else
            stmt.executeUpdate("update DayBook set NonExpence='"+partyIce+"' where name='"+replaceSeller+"' and idd='"+id+"' and category='Party Ice'");
        } else {
            if(!partyIce.equals("")) {
                if(Float.parseFloat(partyIce)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Party Ice',"
                                + "'',"
                                + "'',"
                                + "'"+partyIce+"'"
                                + "); "
                                + ""
                        );
                }
            }
        }
        checker=0;
        insertDayBookBalance(date);
        
    }
    private void insertDayBook(int id, String date, String cashPaid, String partyTruckRent, String linerAmt, String partyExp, String partyIce) throws Exception {
        if(!date.equals("")) {
            if(!cashPaid.equals("")) {
                if(Float.parseFloat(cashPaid)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Cash Paid',"
                                + "'',"
                                + "'',"
                                + "'"+cashPaid+"'"
                                + "); "
                                + ""
                        );
                }
            }
            if(!partyTruckRent.equals("")) {
                if(Float.parseFloat(partyTruckRent)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'PartyTruckRent',"
                                + "'',"
                                + "'',"
                                + "'"+partyTruckRent+"'"
                                + "); "
                                + ""
                        );
                }
            }
            if(!linerAmt.equals("")) {
                if(Float.parseFloat(linerAmt)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Liner Amt',"
                                + "'',"
                                + "'',"
                                + "'"+linerAmt+"'"
                                + "); "
                                + ""
                    );
                }
            }
            if(!partyExp.equals("")) {
                if(Float.parseFloat(partyExp)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Party Exp',"
                                + "'',"
                                + "'',"
                                + "'"+partyExp+"'"
                                + "); "
                                + ""
                    );
                }
            }
            if(!partyIce.equals("")) {
                if(Float.parseFloat(partyIce)!=0) {
                    stmt.executeUpdate("Insert into DayBook ("
                                + "Idd, Name, Date, Category, ToGet, Expence, NonExpence "
                                + ") Values "
                                + "("
                                + "'"+id+"',"
                                + "'"+replaceSeller+"',"
                                + "'"+date+"',"
                                + "'Party Ice',"
                                + "'',"
                                + "'',"
                                + "'"+partyIce+"'"
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
            if(!rs.getString("lastDate").equals(""))
                lastDate = df.parse(rs.getString("lastDate"));
        }
        while(cal.getTime().before(lastDate) || cal.getTime().equals(today) && lastDate != null) {
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
    public void insertFishDetails(String particular, String box, String rate, String rs, String billNo) throws SQLException {
        stmt.executeUpdate("Insert into fish_details ("
                
                + "Name, billNo, Particular, box, rate, rs "
                
                + ") Values "
                + "("
                + "'"+replaceSeller+"',"
                + "'"+billNo+"',"
                + "'"+particular+"',"
                + "'"+box+"',"
                + "'"+rate+"',"
                + "'"+rs+"'"
                + "); "
                + "");
    }
    public void removeFishDetails(String fishId) throws SQLException {
            stmt.executeUpdate("delete from fish_details where name='"+replaceSeller+"' and billNo='"+fishId+"'");
    }
    public String getLastBalance(String date) throws SQLException {
        int id = getLastId(date);
        rs = stmt.executeQuery(" SELECT * FROM "+seller+" WHERE ID = "+id );
        while(rs.next()) {
           return rs.getString("Balance");
        }
        return "0";
    }
    public int getLastId(String date) throws SQLException {
        int id = 1;
        rs = stmt.executeQuery("select * from "+seller+" where date <= '"+date+"' order by id desc limit 1");
        while(rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }
    public int getMaxId() throws SQLException {
        rs = stmt.executeQuery("select max(id) as lstid from "+seller+"");
        while(rs.next()) {
            return rs.getInt("lstid");
        }
        return 0;
    }
    public int getLastBillNo() throws SQLException {
        String date="";
        rs = stmt.executeQuery("select max(Date) from "+seller);
        while(rs.next()) {
            date = rs.getString("max(Date)");
        }
        rs = stmt.executeQuery("select id as lstid from "+seller+" where date = '"+date+"' order by id desc limit 1");
        while(rs.next()) {
            return rs.getInt("lstid");
        }
        return 1;
    }
    
    public void setTotalPercentCutt(String percentCutt, String name) throws SQLException {
        int totalPercentCutt=0;
        rs = stmt.executeQuery("select totalPercentCutt from Sellers where name =  '"+name+"'");
        while(rs.next()){
            totalPercentCutt = rs.getInt("totalPercentCutt");
        }
        totalPercentCutt = totalPercentCutt + Integer.parseInt(percentCutt);
        stmt.executeUpdate("UPDATE Sellers SET totalPercentCutt='"+totalPercentCutt+"' where name='"+name+"'");
    }
    public void setDuplicateAdvance(String advCutt, String name) throws SQLException {
        int duplicateAdv=0;
        rs = stmt.executeQuery("select DuplicateAdvance from Sellers where name =  '"+name+"'");
        while(rs.next()){
            duplicateAdv = rs.getInt("DuplicateAdvance");
        }
        duplicateAdv = duplicateAdv + Integer.parseInt(advCutt);
        stmt.executeUpdate("UPDATE Sellers SET DuplicateAdvance='"+duplicateAdv+"' where name='"+name+"'");
    }
    public void setEditDuplicateAdvance(int advCutt, String name) throws SQLException {
        int duplicateAdv=0;
        rs = stmt.executeQuery("select DuplicateAdvance from Sellers where name =  '"+name+"'");
        while(rs.next()){
            duplicateAdv = rs.getInt("DuplicateAdvance");
        }
        duplicateAdv = duplicateAdv - advCutt;
        stmt.executeUpdate("UPDATE Sellers SET DuplicateAdvance='"+duplicateAdv+"' where name='"+name+"'");
    }
    public ObservableList getFishDetails(String billNo) throws SQLException {
        list.clear();
        rs = stmt.executeQuery("select * from fish_details where name='"+replaceSeller+"' and billNo = '"+billNo+"'");
        while(rs.next()){
            list.add(new BillTableModel(rs.getString("particular"),
                    rs.getString("box"),
                    rs.getString("rate"),
                    rs.getString("rs")));
        }
        return list;
    }
    public ObservableList getEditBillFishDetails(String billNo) throws SQLException {
        list.clear();
//        System.out.println(seller+" "+billNo);
        rs = stmt.executeQuery("select * from fish_details where name='"+replaceSeller+"' and billNo = '"+billNo+"'");
        while(rs.next()){
            list.add(new EditBillTableModel(rs.getString("particular"),
                    rs.getString("box"),
                    rs.getString("rate"),
                    rs.getString("rs"),
                    rs.getString("id")));
        }
        return list;
    }
    public ResultSet getBillDetails(String id) throws SQLException {
        return stmt.executeQuery("select * from "+seller+" where id='"+id+"'");
    }
    public ResultSet getBillDetails(String name, String id) throws SQLException {
        return stmt.executeQuery("select * from AllSellerDetails where billNo='"+id+"' and name='"+name+"'");
    }
    
    // Remove Bill
    
    public boolean checkSales(String billNo) throws SQLException {
        boolean check = false;
        rs = stmt.executeQuery("select * from AllBuyerDetails where sellerName='"+replaceSeller+"' and billNo='"+billNo+"'");
        while(rs.next()){
            check = true;
        }
        return check;
    }
    public void removeBill(String billNo) throws Exception {
        String date="", oldBal="";
        int advCut=0, dupAdv=0;
        if(!billNo.equals("")) {
            rs = stmt.executeQuery("SELECT * FROM "+seller+" WHERE ID = "+billNo);
            while(rs.next()) {
                date = rs.getString("date");
                advCut=rs.getInt("AdvCutting");
            }
            rs = stmt.executeQuery("Select * from Sellers where name='"+replaceSeller+"'");
            while(rs.next()){
                dupAdv= rs.getInt("DuplicateAdvance");
            }
            dupAdv = dupAdv-advCut;
            stmt.executeUpdate("delete from "+seller+" where id="+billNo);
            stmt.executeUpdate("delete from AllSellerDetails where billNo='"+billNo+"' and name='"+replaceSeller+"'");
            stmt.executeUpdate("delete from DayBook where idd='"+billNo+"' and name='"+replaceSeller+"' ");
            oldBal = getLastBalance(date);
            if(Integer.parseInt(oldBal) > 0) 
                stmt.executeUpdate("update BalanceSheet set  toGive='"+oldBal+"', toGet=''  where Name='"+replaceSeller+"'");
            else 
                stmt.executeUpdate("update BalanceSheet set toGive='', toGet='"+oldBal.replaceAll("-", "")+"'  where Name='"+replaceSeller+"'");
            stmt.executeUpdate("UPDATE Sellers SET DuplicateAdvance='"+dupAdv+"', billNo = '"+getLastBillNo()+"', balance = '"+oldBal+"' where name = '"+replaceSeller+"'");
            updateDB(date, oldBal);
            insertDayBookBalance(date);
        }
    }
    
    
    
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(SingleSellerDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private ObservableList list;
    private String seller="", replaceSeller="";
}
