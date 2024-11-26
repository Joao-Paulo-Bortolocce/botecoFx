package com.example.botecofx;

import com.example.botecofx.db.util.SingletonDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Map;
import java.util.ResourceBundle;

public class AdmController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }
    public void onCadGarcom(ActionEvent actionEvent) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("garcom-consulta-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
    }

    public void onCadTipoPagamento(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("tipopagamento-consulta-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
    }

    public void onCadUnidade(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("unidade-consulta-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
    }

    public void onCadCategoria(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("categoria-consulta-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
    }

    public void onCadProduto(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("produto-consulta-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
    }

    public void onRelListaPreco(ActionEvent actionEvent) {
        String sql= "select * from produto,categoria WHERE produto.cat_id=categoria.cat_id order by cat_nome,prod_nome";
        gerarRelatorio(sql,"reports/lista_precos.jasper","Relação simples de produtos");
    }

    public void onRelProdutos(ActionEvent actionEvent) {
        String sql= "select * from produto,categoria WHERE produto.cat_id=categoria.cat_id order by prod_nome";
        gerarRelatorio(sql,"reports/relProdutos.jasper","Relação simples de produtos");
    }

    private void gerarRelatorio(String sql,String relat, String titulo)
    {
        try
        { //sql para obter os dados para o relatorio
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            //implementação da interface JRDataSource para DataSource ResultSet
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            //chamando o relatório
            String jasperPrint =
                    JasperFillManager.fillReportToFile(relat,null, jrRS);
            JasperViewer viewer = new JasperViewer(jasperPrint, false, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//maximizado
            viewer.setTitle(titulo);//titulo do relatório
            viewer.setVisible(true);
        } catch (JRException erro)
        {  erro.printStackTrace(); }

    }

    public void onRestore(ActionEvent actionEvent) {
        try {
            SingletonDB.restaurar("botecodb123.backup","botecodb");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Erro "+e);
        }
    }

    public void onBackup(ActionEvent actionEvent) {
        try {
            SingletonDB.backup("botecodb123.backup","botecodb");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Erro "+e);
        }

    }

    public void onSobre(ActionEvent actionEvent) {
    }

    public void onAjuda(ActionEvent actionEvent) {
        WebView webView = new WebView();
        webView.setPrefSize(800,600);
        //webView.getEngine().load("http://unoeste.br");
        File file = new File("help/ajuda.html");
        webView.getEngine().load(file.toURI().toString());
        Stage stage = new Stage();
        stage.setScene(new Scene(webView));
        stage.showAndWait();
    }
}
