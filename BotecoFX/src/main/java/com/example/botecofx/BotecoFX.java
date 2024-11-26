package com.example.botecofx;

import com.example.botecofx.db.dals.CategoriaDAL;
import com.example.botecofx.db.dals.ComandaDAL;
import com.example.botecofx.db.dals.GarconDAL;
import com.example.botecofx.db.dals.ProdutoDAL;
import com.example.botecofx.db.entidades.Categoria;
import com.example.botecofx.db.entidades.Comanda;
import com.example.botecofx.db.entidades.Produto;
import com.example.botecofx.db.util.SingletonDB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BotecoFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("adm-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(BotecoFX.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BotecoFX");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        String mydatabase="botecodb";
        if(SingletonDB.conectar())
            launch(args);
        else
        {
            JOptionPane.showMessageDialog(null,
                    "Problemas ao conectar: "+SingletonDB.getConexao().getMensagemErro());
            if(JOptionPane.showConfirmDialog(null, "Confirma a tentativa de criação de uma nova base de dados")==JOptionPane.YES_OPTION)
            {
                if(SingletonDB.criarBD(mydatabase))
                    try
                    {
                        SingletonDB.restaurar("bkpinicial.backup",mydatabase);
                    }
                    catch(Exception e) {
                        throw new RuntimeException(e);
                    }
            }
            Platform.exit();
       }
    }
}