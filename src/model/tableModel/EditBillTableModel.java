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
 * @author USER
 */
public class EditBillTableModel {
    
    private StringProperty FishName, BoxCount, UnitPrice, Rs, FishId;

    public EditBillTableModel(String fishName,String boxCount, String unitPrice, String rs, String fishId){
        this.FishName = new SimpleStringProperty(fishName);
        this.BoxCount = new SimpleStringProperty(boxCount);
        this.UnitPrice = new SimpleStringProperty(unitPrice);
        this.Rs = new SimpleStringProperty(rs);
        this.FishId = new SimpleStringProperty(fishId);
        
    }
    public String getFishName(){
        return FishName.get();
    }
    public void setFishName(String fishName){
        FishName.set(fishName);
    }
    
    public String getBoxCount(){
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
    
    public String getFishId() {
        return FishId.get();
    }
    public void setFishId(String fishId) {
        FishId.set(fishId);
    }
}
