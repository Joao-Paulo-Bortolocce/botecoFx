package com.example.botecofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private PasswordField pfSenha;

    @FXML
    void onAcessar(ActionEvent event) throws IOException {
        String view="";
        if(pfSenha.getText().equals("adm")){
            //usuario adm
            view= "adm-view.fxml";
        }
        if(pfSenha.getText().equals("123")){
            //usuario adm
            view= "comanda-painel-view.fxml";
        }
        if(!view.isEmpty()){
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource(view));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("BotecoFX");
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }

    }

}
