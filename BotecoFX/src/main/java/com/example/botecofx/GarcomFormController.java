package com.example.botecofx;

import com.example.botecofx.db.dals.GarconDAL;
import com.example.botecofx.db.entidades.Garcon;
import com.example.botecofx.db.util.SingletonDB;
import com.example.botecofx.util.ApisService;
import com.example.botecofx.util.MaskFieldUtil;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GarcomFormController implements Initializable {

    public ImageView imageView;
    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfCep;

    @FXML
    private TextField tfCidade;

    @FXML
    private TextField tfCpf;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfNumero;

    @FXML
    private TextField tfRua;

    @FXML
    private TextField tfTelefone;

    @FXML
    private TextField tfUf;

    private static File file=null;

    private boolean isEdicao=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() { // espera a tela carregar completamente e realiza essa ação
                tfNome.requestFocus();
            }
        });
        MaskFieldUtil.cpfField(tfCpf);
        MaskFieldUtil.cepField(tfCep);
        MaskFieldUtil.foneField(tfTelefone);
        // Se for alteração, os dados devem ser preenchidos
        if(GarcomConsultaController.garcon!=null){
            isEdicao=true;
            Garcon aux= GarcomConsultaController.garcon;
            tfNome.setText(aux.getNome());
            tfId.setText(""+aux.getId());
            tfId.setEditable(false);
            tfCep.setText(aux.getCep());
            tfCpf.setText(aux.getCpf());
            tfRua.setText(aux.getEndereco());
            tfCidade.setText(aux.getCidade());
            tfNumero.setText(aux.getNumero());
            tfTelefone.setText(aux.getFone());
            tfUf.setText(aux.getUf());
            //Coloca imagem na interface
            BufferedImage bufferedImage= SingletonDB.getConexao().carregarImagem("garcon","gar_foto","gar_id", aux.getId());
            //BufferedImage bufferedImage= SingletonDB.getConexao().carregarImagem("garcon","gar_foto","gar_id", Integer.parseInt( tfId.getId()));
            if (bufferedImage!=null)
                imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        btCancelar.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        Garcon garcon= new Garcon(tfNome.getText(),tfCpf.getText(),tfCep.getText(),tfRua.getText(),tfNumero.getText(),tfCidade.getText(),tfUf.getText(),tfTelefone.getText());
        if(file==null)
            file= new File(getClass().getResource("/com/example/botecofx/semFoto.jpg").getFile());
        if(isEdicao){
            garcon.setId(GarcomConsultaController.garcon.getId());
            if(!new GarconDAL().alterar(garcon,file)){
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao alterar garçom "+ SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        }
        else
            if(!new GarconDAL().gravar(garcon,file)){
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao gravar garçom "+ SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
        btConfirmar.getScene().getWindow().hide();
    }

    public void onBuscarCep(KeyEvent keyEvent) {
        String dados;
        if(tfCep.getText().length()==9){
            dados=ApisService.consultaCep(tfCep.getText(),"json");
            JSONObject json= new JSONObject(dados);
            tfCidade.setText(json.getString("localidade"));
            tfRua.setText(json.getString("logradouro"));
            tfUf.setText(json.getString("uf"));
        }
    }

    public void onBuscarImagem(MouseEvent mouseEvent) {
        FileChooser fileChooser= new FileChooser();
        file=fileChooser.showOpenDialog(null);
        if(file!=null){
            Image image= new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }
}
