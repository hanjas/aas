/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aas.fish.merchant;

import backend.DBAccess;
import com.sun.javafx.stage.StageHelper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Haxx
 */
public class AASFishMerchant extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AAS Fish Merchant");
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
            Scene myScene = new Scene(myPane,800,400);
            primaryStage.setScene(myScene);
            primaryStage.getIcons().add(new Image("/image/aas.png"));
            primaryStage.setMaximized(true);
            DBAccess db = new DBAccess();
            db.close();
            primaryStage.show();
            primaryStage.setMinHeight(450);
            primaryStage.setMinWidth(900);
            
            primaryStage.iconifiedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    System.out.println("Iconified? " + newValue);
                    for(Stage stage : StageHelper.getStages()) {
                        stage.setIconified(newValue);
                    }
                }
            });
            
            
            primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DBAccess db = new DBAccess();
                                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                db.insertDayBookBalance(date);
                                db.close();
                            } catch (Exception ex) {
                                Logger.getLogger(AASFishMerchant.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.exit(0);
                        }
                    });
                }
            });
            
            
        }catch (Exception e) { System.out.println(e); }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
