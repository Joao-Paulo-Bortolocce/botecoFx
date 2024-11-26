package com.example.botecofx;

import com.example.botecofx.db.dals.ComandaDAL;
import com.example.botecofx.db.entidades.Comanda;
import com.example.botecofx.db.util.SingletonDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.tools.ant.taskdefs.Sleep;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComandaPainelController implements Initializable {
    static public ComandaDAL comandaDAL;

    static public Comanda comanda;
    public FlowPane flowPane;
    public Button btMostrarComanda;
    public DatePicker dpDataInicio;
    public DatePicker dpDataFim;
    public static Button btTodas;
    private boolean mostrarTudo;
    private boolean todas;
    private LocalDate inicio;
    private LocalDate fim;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //obter as comandas em aberto do banco
        // colocar comanda na tela
        todas=true;
        inicio=LocalDate.of(0,1,1);
        fim=LocalDate.now();
        comanda= new Comanda();
        comandaDAL = new ComandaDAL();
        carregarComandas();
        mostrarTudo=false;
        escreverBotao();
    }

    private void escreverBotao() {
        if (!mostrarTudo){
            btMostrarComanda.setText("Mostrar todas as comandas");
        }
        else {
            btMostrarComanda.setText("Mostrar apenas as abertas");
        }
    }

    public void carregarComandas(){
        try{

            flowPane.getChildren().clear();
            List<Comanda> comandaList = comandaDAL.get("");
             for (Comanda comanda : comandaList) {
                 if(mostrarTudo || comanda.getStatus()=='A') {
                     if(todas || (inicio!=null && fim!=null && ((comanda.getData().isEqual(inicio)||comanda.getData().isAfter(inicio))&&(comanda.getData().isEqual(fim)||comanda.getData().isBefore(fim))))) {
                         ComandaController.status = comanda.getStatus();
                         ComandaController.numgeral = comanda.getId();
                         FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
                         Parent root = (Parent) loader.load();
                         ComandaController ctr = loader.getController();
                         ctr.setNumeroComanda(comanda.getId());  // executa o método
                         flowPane.getChildren().add(root);
                     }
                 }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void onNovaComanda(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("garcom-seletor-view.fxml"));
        ComandaController.comanda=comanda;
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("BotecoFX");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();
        if(comanda.getGarcon()!=null){
            int num=0;
            List<Comanda> comandaList = comandaDAL.get("");
            List<Integer> nums= new ArrayList<>();
            for (Comanda comanda : comandaList)
                nums.add(comanda.getNumero());
            nums.sort(null);
            for (num = 1; num <= nums.size() && nums.get(num-1).equals(num); num++) ;
            comanda.setNumero(num);
            comandaDAL.gravar(comanda);
            comanda= comandaDAL.get(SingletonDB.getConexao().getMaxPK("comanda","com_id"));
            ComandaController.comanda=comanda;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("comanda-view.fxml"));
            Parent root = (Parent) loader.load();
            ComandaController ctr = loader.getController();
            ctr.setNumeroComanda(0);


        }
        carregarComandas();
    }

    public void onMostrarComandas(ActionEvent actionEvent) {
        mostrarTudo=!mostrarTudo;
        escreverBotao();
        carregarComandas();
    }


    public void onSelecionaDataInicio(ActionEvent actionEvent) {
        inicio=dpDataInicio.getValue();

        if(inicio!=null && fim!=null && inicio.isAfter(fim)){
            inicio=fim;
            dpDataInicio.setValue(inicio);
        }
        else
            todas=false;
        carregarComandas();
    }

    public void onSelecionaDataFim(ActionEvent actionEvent) {
        fim=dpDataFim.getValue();
        if (inicio!=null && fim!=null && fim.isBefore(inicio)){
            fim=inicio;
            dpDataFim.setValue(fim);
        }
        else
            todas=false;
        carregarComandas();
    }

    public void onHoje(ActionEvent actionEvent) {
        fim=LocalDate.now();
        inicio=LocalDate.now();
        dpDataFim.setValue(fim);
        dpDataInicio.setValue(inicio);
        todas=false;
        carregarComandas();
    }

    public void onTodas(ActionEvent actionEvent) {
        todas=true;
        dpDataFim.setValue(null);
        dpDataInicio.setValue(null);
        inicio=LocalDate.of(0,1,1);
        fim=LocalDate.now();
        carregarComandas();
    }
}
