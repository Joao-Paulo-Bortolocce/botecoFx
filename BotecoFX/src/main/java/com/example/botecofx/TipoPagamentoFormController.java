package com.example.botecofx;

import com.example.botecofx.db.dals.TipoPagamentoDAL;
import com.example.botecofx.db.entidades.Garcon;
import com.example.botecofx.db.entidades.TipoPagamento;
import com.example.botecofx.db.util.SingletonDB;
import com.example.botecofx.util.MaskFieldUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TipoPagamentoFormController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    private boolean isAlterar=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() { // espera a tela carregar completamente e realiza essa ação
                tfNome.requestFocus();
            }
        });
        tfId.setEditable(false);
        // Se for alteração, os dados devem ser preenchidos
        if(TipoPagamentoConsultaController.tipoPagamento!=null){
            TipoPagamento aux= TipoPagamentoConsultaController.tipoPagamento;
            tfNome.setText(aux.getNome());
            tfId.setText(""+aux.getId());
            isAlterar=true;
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        btConfirmar.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        TipoPagamento tipoPagamento = new TipoPagamento(Integer.parseInt(tfId.getText()),tfNome.getText());
        if (isAlterar) {
            if (!new TipoPagamentoDAL().alterar(tipoPagamento)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao gravar Tipo de pagamento " + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        }
        else
            if(!new TipoPagamentoDAL().gravar(tipoPagamento)){
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao gravar Tipo de pagamento "+ SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        btConfirmar.getScene().getWindow().hide();
    }

}
