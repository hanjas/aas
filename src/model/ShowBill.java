/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author BCz
 */
public class ShowBill extends BorderPane{

    public ShowBill() {
        companyName = new Label("AAS Kannur");
        companyDesc = new Label("Ice Fish Merchant & Commission Agent");
        companyAddr = new Label("AYIKKARA, KANNUR -17");
        phno = new Label("Mob: 9447772793");
        
        topGrid = new GridPane();
        topBorder = new BorderPane();
        
        companyName.setStyle("-fx-font-size: 18px Segoe UI Bold;");
        companyDesc.setStyle("-fx-font-size: 12px;");
        companyAddr.setStyle("-fx-font: 15px Segoe UI Bold;");
        phno.setStyle("-fx-font-size: 12px;");
        companyName.setAlignment(Pos.CENTER);
        companyDesc.setAlignment(Pos.CENTER);
        companyAddr.setAlignment(Pos.CENTER);
        phno.setAlignment(Pos.CENTER);
        companyName.setMaxWidth(Double.MAX_VALUE);
        companyDesc.setMaxWidth(Double.MAX_VALUE);
        companyAddr.setMaxWidth(Double.MAX_VALUE);
        phno.setMaxWidth(Double.MAX_VALUE);
        
        topGrid.add(companyName, 0, 0);
        topGrid.add(companyDesc, 0, 1);
        topGrid.add(companyAddr, 0, 2);
        topGrid.add(phno, 0, 3);
        topGrid.setAlignment(Pos.CENTER);
        topGrid.setVgap(10);
        
        topBorder.setCenter(topGrid);
        setTop(topBorder);
        setPadding(new Insets(10));
        setPrefSize(500, 600);
    }
    
    private Label companyName, companyDesc, companyAddr, phno;
    private GridPane topGrid;
    private BorderPane topBorder;
}
