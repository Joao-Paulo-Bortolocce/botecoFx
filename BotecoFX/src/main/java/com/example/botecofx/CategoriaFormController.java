package com.example.botecofx;

import com.example.botecofx.db.dals.CategoriaDAL;
import com.example.botecofx.db.dals.UnidadeDAL;
import com.example.botecofx.db.entidades.Categoria;
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

public class CategoriaFormController implements Initializable {

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
        if(CategoriaConsultaController.categoria!=null){
            Categoria aux= CategoriaConsultaController.categoria;
            tfNome.setText(aux.getNome());
            tfId.setText(""+aux.getId());
            isAlterar=true;
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        tfNome.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        Categoria categoria= new Categoria(Integer.parseInt(tfId.getText()),tfNome.getText());
        if (isAlterar) {
            if (!new CategoriaDAL().alterar(categoria)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao gravar categoria " + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        }
        else
            if (!new CategoriaDAL().gravar(categoria)){
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao gravar categoria "+ SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        btCancelar.getScene().getWindow().hide();
    }

}
