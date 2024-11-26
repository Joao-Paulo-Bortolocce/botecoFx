package com.example.botecofx;

import com.example.botecofx.db.dals.*;
import com.example.botecofx.db.entidades.*;
import com.example.botecofx.db.util.SingletonDB;
import com.example.botecofx.util.ModalTable;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ComandaFormController implements Initializable {

    public TableColumn<Comanda.Item,String> colProduto;
    public TableColumn<Comanda.Item,String> colQuantidade;
    public TableColumn<Comanda.Item,String> colValor;
    public Label lbValorTotal;
    public Label lbValorPago;
    public Button btPagar;
    @FXML
    private Button btAdd;

    @FXML
    private Button btFinalizar;

    @FXML
    private Button btProdutos;

    @FXML
    private ComboBox<Garcon> cbGarcom;

    @FXML
    private ComboBox<TipoPagamento> cbTipoPagamento;

    @FXML
    private DatePicker dpData;

    @FXML
    private Label lbValor;

    @FXML
    private Spinner<Integer> spQtd;

    @FXML
    private TextArea taDesc;

    @FXML
    private TableView<Comanda.Item> tabela;

    @FXML
    private TextField tfNumero;

    private Produto produto = null;

    private boolean isAberta=true;
    Comanda comanda;
    double valor;
    public static double restante;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comanda = new ComandaDAL().get(ComandaController.comanda.getId());

        spQtd.setVisible(false);
        if(comanda.getStatus()=='F'){
            isAberta=false;
        }

        tfNumero.setEditable(isAberta);
        dpData.setEditable(isAberta);
        cbGarcom.setEditable(isAberta);
        cbTipoPagamento.setEditable(isAberta);
        taDesc.setEditable(isAberta);
        tfNumero.setEditable(isAberta);
        tfNumero.setEditable(isAberta);
        btFinalizar.setVisible(isAberta);
        btAdd.setVisible(isAberta);
        btPagar.setVisible(isAberta);

        // Configurar a coluna Produto usando Callback
        colProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().produto().getNome()));
        colQuantidade.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().quant())));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().valor())));
        tfNumero.setText(""+comanda.getNumero());
        dpData.setValue(comanda.getData());
        preencherTabela();
        preencherGarcon();
        preencherPagamento();
        Garcon garcon = comanda.getGarcon();
        if(garcon!=null)
            cbGarcom.setValue(garcon);
        taDesc.setText(comanda.getDescricao());
        spQtd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
    }

    private void preencherGarcon() {
        if (isAberta){
            List<Garcon> garconList= new GarconDAL().get("");
            ObservableList garcons= FXCollections.observableArrayList();
            for (Garcon gar:garconList)
                garcons.add(gar);
            cbGarcom.setItems(garcons);
        }

    }

    private void preencherPagamento() {
        if (isAberta){
            List<TipoPagamento> tipoPagamentoList= new TipoPagamentoDAL().get("");
            ObservableList tipos= FXCollections.observableArrayList();
            for (TipoPagamento tp:tipoPagamentoList)
                tipos.add(tp);
            cbTipoPagamento.setItems(tipos);
        }

    }

    private void preencherTabela() {
        valor=0;
        double valorPago=0,valorTotal=0;
         List<Comanda.Item> itens = new ArrayList<>();
        if(!comanda.getItens().isEmpty())
            itens.addAll(comanda.getItens());

        for (Comanda.Item item:itens){
            valorTotal+= item.valor()*item.quant();
        }
        List<Pagamento> pagamentoList= comanda.getPagamentos();
        if(pagamentoList.size()>0){
            for (Pagamento pag:pagamentoList)
                valorPago+=pag.getValor();
        }
        valor=valorTotal-valorPago;
        restante=valor;

        tabela.setItems(FXCollections.observableArrayList(itens));
        lbValor.setText("R$ "+valor);
        lbValorPago.setText("R$ "+valorPago);
        lbValorTotal.setText("R$ "+valorTotal);
    }

    @FXML
    void onAddProd(ActionEvent event) {
        int i,qtd=0;
        Comanda.Item item;
        for ( i= 0; i < comanda.getItens().size() && comanda.getItens().get(i).produto().getId()!=produto.getId(); i++) ;
        if(i< comanda.getItens().size()) {
            item = new Comanda.Item(produto,  comanda.getItens().get(i).quant() + spQtd.getValue(),  comanda.getItens().get(i).valor());
            comanda.getItens().remove(i);
            comanda.getItens().add(i,item);
        }
        else{
            item= new Comanda.Item(produto,spQtd.getValue(),produto.getPreco());
            comanda.getItens().add(item);
        }
        preencherTabela();
    }

    @FXML
    void onCancelar(ActionEvent event) {
        lbValor.getScene().getWindow().hide();
    }

    @FXML
    void onCofirmar(ActionEvent event) {
        if (isAberta){
            comanda.setGarcon(cbGarcom.getValue());
            comanda.setData(dpData.getValue());
            comanda.setDescricao(taDesc.getText());
            comanda.setValor(valor);
            new ComandaDAL().alterar(comanda);
        }


        lbValor.getScene().getWindow().hide();
    }

    @FXML
    void onFinishComanda(ActionEvent event) throws IOException {
        onPagar(null);
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Deseja realmente fechar a comanda?");
        if(alert.showAndWait().get()== ButtonType.OK){
            comanda.setValor(0);
            comanda.setStatus('F');
            onCofirmar(null);
        }

    }

    @FXML
    void onImprimir(ActionEvent event) {
        HashMap hashMap = new HashMap();
        hashMap.put("comanda_id",ComandaController.comanda.getId());
        gerarRelatorioSubReport("reports/comanda_print.jasper","Comanda",hashMap);
    }

    @FXML
    void onSelProd(ActionEvent event) {
        if(isAberta){
            ModalTable modalTable = new ModalTable(new ProdutoDAL().get(""),new String[]{"nome","preco","categoria"},"nome");
            Stage stage = new Stage();
            stage.setScene(new Scene(modalTable));
            stage.setWidth(600); stage.setHeight(480); stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            produto=(Produto)modalTable.getSelecionado();
            btProdutos.setText(produto.getNome());
            if(produto!=null){
                spQtd.setVisible(true);
                spQtd.getValueFactory().setValue(1);
            }
        }

    }

    public void onFechar(ActionEvent actionEvent) {
        lbValor.getScene().getWindow().hide();
    }

    private void gerarRelatorioSubReport(String relat, String titulotela, Map parametros)
    {
        try {

            JasperPrint jasperPrint = JasperFillManager.fillReport(relat, parametros,
                    SingletonDB.getConexao().getConnect());
            JasperViewer viewer = new JasperViewer(jasperPrint,false);

            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//maximizado
            viewer.setTitle(titulotela);
            viewer.setVisible(true);
        } catch (JRException erro) {
            System.out.println(erro);
        }
    }

    public void onPagar(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("pagamento-form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("BotecoFX");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);
        stage.showAndWait();
        if(PagamentoFormView.valor>0){
            Object pagAux= cbTipoPagamento.getValue();
            String aux = pagAux.toString();
            List<TipoPagamento> tipoPagamentoList= new TipoPagamentoDAL().get(aux);

            if (tipoPagamentoList.size()>0){
                comanda.addPagamento(tipoPagamentoList.get(0),PagamentoFormView.valor);
                preencherTabela();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Voce nao selecionou a forma de pagamento");
            }
        }
    }
}
