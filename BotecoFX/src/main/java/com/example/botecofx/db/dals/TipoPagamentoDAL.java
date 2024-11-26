package com.example.botecofx.db.dals;

import com.example.botecofx.db.entidades.Categoria;
import com.example.botecofx.db.entidades.TipoPagamento;
import com.example.botecofx.db.util.IDAL;
import com.example.botecofx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoPagamentoDAL implements IDAL<TipoPagamento> {

    @Override
    public boolean gravar(TipoPagamento entidade){
        String sql = """ 
                INSERT INTO tipopgto(tpg_nome) VALUES ('#1');
                """;
        sql =sql.replace("#1",entidade.getNome());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(TipoPagamento entidade) {
        String sql = """ 
                UPDATE tipopgto SET tpg_nome='#1' WHERE  tpg_id='#2'
                """;
        sql =sql.replace("#1",entidade.getNome());
        sql =sql.replace("#2",""+entidade.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(TipoPagamento entidade){
        return SingletonDB.getConexao().manipular("DELETE FROM tipopgto WHERE tpg_id="+entidade.getId());
    }

    @Override
    public TipoPagamento get(int id){
        String sql ="SELECT * FROM tipopgto WHERE  tpg_id="+id;
        TipoPagamento tipoPagamento=null;
        ResultSet resultSet=SingletonDB.getConexao().consultar(sql);
        try{
            if(resultSet.next()){
                tipoPagamento=new TipoPagamento(id,resultSet.getString("tpg_nome"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tipoPagamento;
    }

    @Override
    public List<TipoPagamento> get(String filtro){
        List<TipoPagamento> lista= new ArrayList<>();
        String sql="SELECT * FROM tipopgto";
        if(!filtro.isEmpty())
            sql+=" WHERE tpg_nome LIKE '%"+filtro+"%'";
        sql+=" ORDER BY tpg_nome";
        ResultSet resultSet=SingletonDB.getConexao().consultar(sql);
        try{
            while(resultSet.next()){
                lista.add(new TipoPagamento(resultSet.getInt("tpg_id"), resultSet.getString("tpg_nome")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lista;
    }
}
