package com.example.botecofx;

import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.TipoPagamento;
import com.example.botecofx.db.entidades.Unidade;
import com.example.botecofx.db.util.SingletonDB;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UnidadeFormController implements Initializable {

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
        if(UnidadeConsultaController.unidade!=null){
            Unidade aux= UnidadeConsultaController.unidade;
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
        Unidade unidade= new Unidade(Integer.parseInt(tfId.getText()),tfNome.getText());
        if(isAlterar){
            if (!new UnidadeDAL().alterar(unidade)){
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao gravar unidade "+ SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        }
        else
            if (!new UnidadeDAL().gravar(unidade)){
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao gravar unidade "+ SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        btCancelar.getScene().getWindow().hide();
    }

}
