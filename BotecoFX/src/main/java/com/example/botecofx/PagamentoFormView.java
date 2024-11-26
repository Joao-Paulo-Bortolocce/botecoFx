package com.example.botecofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PagamentoFormView implements Initializable {

    public TextField tfValor;
    @FXML
    private Label lbValorRestante;
    public static double valor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbValorRestante.setText("Valor restante R$ "+ComandaFormController.restante);
    }

    @FXML
    void onCancelar(ActionEvent event) {
        valor=0;
        lbValorRestante.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        valor=Double.parseDouble(tfValor.getText());
        if(valor>ComandaFormController.restante){
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja Pagar o valor restante da conta: R$ "+ComandaFormController.restante);
            if(alert.showAndWait().get()== ButtonType.OK)
                valor=ComandaFormController.restante;
            else
                valor=0;
        }
        lbValorRestante.getScene().getWindow().hide();
    }

}
