/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.alert;

import javafx.scene.control.Alert;

/**
 *
 * @author BCz
 */
public class SavedPopup extends Alert {
    
    public SavedPopup() {
        super(AlertType.INFORMATION);
        setTitle("Saved");
        setHeaderText(null);
        setContentText("Saved");
    }
    
}
