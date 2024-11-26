package com.example.botecofx;

import com.example.botecofx.db.dals.GarconDAL;
import com.example.botecofx.db.entidades.Garcon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GarcomConsultaController implements Initializable {


    @FXML
    public TableView<Garcon> tabela;

    @FXML
    private TableColumn<Garcon, String> col_cidade;

    @FXML
    private TableColumn<Garcon, String> col_id;

    @FXML
    private TableColumn<Garcon, String> col_nome;

    @FXML
    private TableColumn<Garcon, String> col_telefone;

    @FXML
    private TextField tfFiltro;

    private GarconDAL garconDAL;

    public static Garcon garcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        garconDAL= new GarconDAL();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("fone"));
        preencherTabela("");
    }

    private void preencherTabela(String filtro) {
        List<Garcon>garconList= garconDAL.get(filtro);
        tabela.setItems(FXCollections.observableArrayList(garconList));
    }

    @FXML
    void onAlterar(ActionEvent event) throws Exception{
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            garcon=tabela.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("garcom-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            garcon=null;
            preencherTabela("");
            tfFiltro.setText("");
        }

    }

    @FXML
    void onExcluir(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            Garcon garcon=  tabela.getSelectionModel().getSelectedItem();
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja excluir o garçom "+garcon.getNome());
            if(alert.showAndWait().get()== ButtonType.OK){
                garconDAL.apagar(garcon);
                preencherTabela("");
                onFiltro(null);
            }
        }
    }

    @FXML
    void onFechar(ActionEvent event) {
        tfFiltro.getScene().getWindow().hide();
    }

    @FXML
    void onFiltro(KeyEvent event) {
        String filtro= tfFiltro.getText().toUpperCase();
        preencherTabela("upper(gar_nome) LIKE'"+filtro+"'");
    }

    @FXML
    void onNovo(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("garcom-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        preencherTabela("");
        tfFiltro.setText("");
    }

}
