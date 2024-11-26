package com.example.botecofx.db.dals;

import com.example.botecofx.db.entidades.Comanda;
import com.example.botecofx.db.entidades.Produto;
import com.example.botecofx.db.entidades.Unidade;
import com.example.botecofx.db.util.IDAL;
import com.example.botecofx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItensDAL {


    public boolean gravar(Comanda.Item entidade) {
        return false;
    }


    public boolean alterar(Comanda.Item entidade) {
        return false;
    }


    public boolean apagar(Comanda.Item entidade) {
        return false;
    }





    public List<Comanda.Item> get(int id) {
        List<Comanda.Item> itemList= new ArrayList<>();
        String sql="SELECT * FROM item";
        sql+=" WHERE com_id="+id;
        ResultSet resultSet= SingletonDB.getConexao().consultar(sql);
        try{
            while(resultSet.next()){
                Comanda.Item item=null;
                Produto produto = new ProdutoDAL().get(resultSet.getInt("prod_id"));
                item = new Comanda.Item(produto,resultSet.getInt("it_quantidade"),resultSet.getDouble("it_valor"));
                itemList.add(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemList;
    }
}
