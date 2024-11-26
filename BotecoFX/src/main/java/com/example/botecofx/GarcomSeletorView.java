package com.example.botecofx;

import com.example.botecofx.db.dals.GarconDAL;
import com.example.botecofx.db.entidades.Categoria;
import com.example.botecofx.db.entidades.Garcon;
import com.example.botecofx.db.entidades.Unidade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GarcomSeletorView implements Initializable {

    @FXML
    private ComboBox<Garcon> cbGarcom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Garcon> garconList = new GarconDAL().get("");
        ObservableList garcons= FXCollections.observableArrayList();
        for(Garcon garcon:garconList){
            garcons.addAll(garcon);
        }
        cbGarcom.setItems(garcons);
    }

    public void onConfirmar(ActionEvent actionEvent) {
        ComandaPainelController.comanda.setGarcon(cbGarcom.getValue());
        cbGarcom.getScene().getWindow().hide();
    }

    public void onCancelar(ActionEvent actionEvent) {
        cbGarcom.getScene().getWindow().hide();
    }
}
