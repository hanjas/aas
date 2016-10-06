/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.edit;

import backend.DBAccess;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ListWithDeleteAndRemoveBtn;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class EditSellerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = FXCollections.observableArrayList();
        refreshItems();
        setItems();
        searchFieldAction();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) createBtn.getScene().getWindow();
                myStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, evt->{
                    if( evt.getCode() == KeyCode.F1 ) {
                        createSeller();
                    }
                });
            }
        });
    }    
    public void searchFieldAction() {
        searchField.textProperty().addListener(obs->{
            String filter = searchField.getText(); 
            if(filter != null || filter.length() != 0) {
                filterItems(filter.toUpperCase());
            }
        });
    }
    public void filterItems(String searchName) {
        list.clear();
        try {
            DBAccess db = new DBAccess();
            list = db.getSellersNames();
            FilteredList<String> filteredData = new FilteredList<>(list, s -> true);
            filteredData.setPredicate(s -> s.contains(searchName));
            List<ListWithDeleteAndRemoveBtn> arrayList = new ArrayList<>();
            for(Object name : filteredData) {
                ListWithDeleteAndRemoveBtn listBtn = new ListWithDeleteAndRemoveBtn(name.toString(), "seller", this);
                arrayList.add(listBtn);
            }
            ObservableList<ListWithDeleteAndRemoveBtn> myObservableList = FXCollections.observableList(arrayList);
            listView.setItems(myObservableList);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(EditSellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void refreshItems() {
        list.clear();
        try {
            DBAccess db = new DBAccess();
            list = db.getSellersNames();
            List<ListWithDeleteAndRemoveBtn> arrayList = new ArrayList<>();
            for(Object name : list) {
                ListWithDeleteAndRemoveBtn listBtn = new ListWithDeleteAndRemoveBtn(name.toString(), "seller", this);
                arrayList.add(listBtn);
            }
            myObservableList = FXCollections.observableList(arrayList);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(EditSellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setItems() {
        listView.setItems(myObservableList);
    }
    @FXML public void createSeller() {
        Stage seller = new Stage();
        seller.setTitle("Create Seller");
        seller.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/CreateSeller.fxml"));
            Scene myScene = new Scene(myPane);
            seller.setScene(myScene);
            seller.setMaximized(false);
            seller.setMinHeight(450);
            seller.setMinWidth(450);
            seller.initOwner(myStage);
            seller.showAndWait();
            refreshItems();
            setItems();
        }catch (Exception e) { System.out.println(e); }
    }
    @FXML public void createSellerOnKeyPress(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER)
            createSeller();
    }
    
    @FXML ListView<ListWithDeleteAndRemoveBtn> listView;
    @FXML TextField searchField;
    @FXML Button createBtn;
    private Stage myStage;
    private ObservableList list;
    ObservableList<ListWithDeleteAndRemoveBtn> myObservableList;
}
