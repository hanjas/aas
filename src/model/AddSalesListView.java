/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.insertControllers.InsertOtherDetailsController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author BCz
 */
public class AddSalesListView extends HBox{
    Label nameLabel = new Label();
    Label totalBillLabel = new Label();
    Label cashPaidLabel = new Label();
    Label idLabel = new Label();
    Button addSalesBtn =  new Button("AddSales");
    Stage stage;
    
    public AddSalesListView(String id, String name, String billAmt, String cashPaid, Stage stage) {
        this.stage = stage;
        nameLabel.setText(name);
        totalBillLabel.setText(billAmt);
        cashPaidLabel.setText(cashPaid);
        idLabel.setText(id);
        
        nameLabel.setPrefWidth(237);
        totalBillLabel.setPrefWidth(106);
        cashPaidLabel.setPrefWidth(106);
        nameLabel.setMaxWidth(250);
        totalBillLabel.setMaxWidth(120);
        cashPaidLabel.setMaxWidth(120);
        
        nameLabel.setAlignment(Pos.CENTER);
        totalBillLabel.setAlignment(Pos.CENTER);
        cashPaidLabel.setAlignment(Pos.CENTER);
        
//        setSpacing(10);
        getChildren().addAll(nameLabel, totalBillLabel, cashPaidLabel, addSalesBtn);
        setSpacing(3);
        addSalesAction();
    }

    private void addSalesAction() {
        addSalesBtn.setOnAction(e->{
            try {
                Stage viewLossAndProfit = new Stage();
                viewLossAndProfit.setTitle("Add Sales");
                viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/insert/InsertOtherDetails.fxml"));
                Pane myPane = (Pane)loader.load();
                InsertOtherDetailsController controller = loader.getController();
                controller.setNameAndBillNo(nameLabel.getText().toUpperCase(), idLabel.getText());
                Scene myScene = new Scene(myPane);
                viewLossAndProfit.setScene(myScene);
                viewLossAndProfit.setMaximized(false);
                viewLossAndProfit.initOwner(stage);
                viewLossAndProfit.show();
            } catch (Exception ex) {
                Logger.getLogger(AddSalesListView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
}
