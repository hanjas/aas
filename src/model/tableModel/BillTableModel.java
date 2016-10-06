/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.tableModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Haxx
 */
public class BillTableModel {

    private StringProperty FishName, BoxCount, UnitPrice, Rs;
    
    public BillTableModel(String fishName,String boxCount, String unitPrice, String rs){
        this.FishName = new SimpleStringProperty(fishName);
        this.BoxCount = new SimpleStringProperty(boxCount);
        this.UnitPrice = new SimpleStringProperty(unitPrice);
        this.Rs = new SimpleStringProperty(rs);
        
    }
    public String getFishName(){
        return FishName.get();
    }
    public void setFishName(String fishName){
        FishName.set(fishName);
    }
    
    public String getBoxCount(){
        if(BoxCount.get().equals(""))
            return "0";
        return BoxCount.get();
    }
    public void setBoxCount(String boxCount){
        BoxCount.set(boxCount);
    }
    
    public String getUnitPrice(){
        return UnitPrice.get();
    }
    public void setUnitPrice(String unitPrice){
        UnitPrice.set(unitPrice);
    }
    
    public String getRs() {
        return Rs.get();
    }
    public void setRs(String rs) {
        Rs.set(rs);
    }
}
