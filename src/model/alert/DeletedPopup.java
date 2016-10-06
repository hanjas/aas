/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.alert;

import javafx.scene.control.Alert;

/**
 *
 * @author USER
 */
public class DeletedPopup extends Alert{

    public DeletedPopup() {
        super(AlertType.INFORMATION);
        setTitle("Deleted Message");
        setHeaderText(null);
        setContentText("Deleted");
    }
    
}
