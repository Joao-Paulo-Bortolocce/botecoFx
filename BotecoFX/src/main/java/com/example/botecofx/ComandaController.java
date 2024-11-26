package com.example.botecofx;

import com.example.botecofx.db.dals.ComandaDAL;
import com.example.botecofx.db.entidades.Comanda;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ComandaController implements Initializable {
    public AnchorPane anchorPane;
    public Label lbNumComanda;
    public Label lbValor;
    private int id;
    boolean nova=false;
    static public Comanda comanda;
    static public char status;
    public static int numgeral;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(status=='F'){
            anchorPane.setStyle("-fx-background-color: #E84435; -fx-background-radius: 10");
        }
        Comanda aux = new ComandaDAL().get(numgeral);
        lbNumComanda.setText(""+aux.getNumero());
        //atualizar valor
        lbValor.setText("R$ "+aux.getValor());
    }

    public void onGerenciarComanda(MouseEvent mouseEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("comanda-form-view.fxml"));
        if(!nova)
            comanda= ComandaPainelController.comandaDAL.get(id);
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("BotecoFX");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);
        stage.showAndWait();
        nova=false;
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-painel-view.fxml"));
//        Parent root = loader.load();
//        ComandaPainelController controller = loader.getController();
        numgeral=id;
        this.initialize(null,null);

    }


    public void setNumeroComanda(int id) throws IOException {
        //buscar o numero da comanda de acordo com o numero recebido
        if(id==0){
            nova=true;
            this.onGerenciarComanda(null);
        }
        this.id=id;

    }

}
