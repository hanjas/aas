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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ListWithDeleteAndRemoveBtn;
import model.ListWithEditBtn;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class EditGroupController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = FXCollections.observableArrayList();
        try {
            setListItems();
            searchFieldAction();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    myStage = (Stage) createBtn.getScene().getWindow();
                    myStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, evt->{
                        if( evt.getCode() == KeyCode.F1 ) {
                            createGroup();
                        }
                    });
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(EditGroupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    public void searchFieldAction() {
        searchField.textProperty().addListener(obs->{
            String filter = searchField.getText(); 
            if(filter != null || filter.length() != 0) {
                filterItems(filter.toLowerCase());
            }
        });
    }
    public void filterItems(String searchName) {
        list.clear();
        try {
            DBAccess db = new DBAccess();
            list = db.getGroupesNames();
            FilteredList<String> filteredData = new FilteredList<>(list, s -> true);
            filteredData.setPredicate(s -> s.contains(searchName));
            List<ListWithEditBtn> arrayList = new ArrayList<>();
            for(Object name : filteredData) {
                ListWithEditBtn listBtn = new ListWithEditBtn(name.toString(), "groupe", this, this.listView);
                arrayList.add(listBtn);
            }
            ObservableList<ListWithEditBtn> myObservableList = FXCollections.observableList(arrayList);
            listView.setItems(myObservableList);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(EditSellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setListItems() {
        list.clear();
        try {
            DBAccess db = new DBAccess();
            list = db.getGroupesNames();
            List<ListWithEditBtn> arrayList = new ArrayList<>();
            for(Object name : list) {
                ListWithEditBtn listBtn = new ListWithEditBtn(name.toString(), "groupe", this, this.listView);
                arrayList.add(listBtn);
            }
            ObservableList<ListWithEditBtn> myObservableList = FXCollections.observableList(arrayList);
            listView.setItems(myObservableList);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(EditSellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML public void createGroup() {
        Stage createGroup = new Stage();
        createGroup.setTitle("Create Group");
        createGroup.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/CreateGroup.fxml"));
            Scene myScene = new Scene(myPane);
            createGroup.setScene(myScene);
            createGroup.setMaximized(false);
            createGroup.setMinWidth(300);
            createGroup.setMinHeight(150);
            createGroup.initOwner(myStage);
            createGroup.showAndWait();
            setListItems();
        }catch (Exception e) { System.out.println(e); }
    }
    @FXML public void createGroupOnKeyPress(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER)
            createGroup();
    }
    
    @FXML ListView listView;
    @FXML TextField searchField;
    @FXML Button createBtn;
    private Stage myStage;
    private ObservableList list;
}
