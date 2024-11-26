package com.example.botecofx.db.dals;

import com.example.botecofx.db.entidades.Comanda;
import com.example.botecofx.db.entidades.Garcon;
import com.example.botecofx.db.entidades.Pagamento;
import com.example.botecofx.db.util.IDAL;
import com.example.botecofx.db.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAL implements IDAL<Comanda> {
    @Override
    public boolean gravar(Comanda entidade) {
            boolean erro=false;
        try{
            SingletonDB.getConexao().getConnect().setAutoCommit(false);
            String sql= """
                    INSERT INTO comanda(gar_id, com_numero, com_data, com_desc, com_valor, com_status)
                        VALUES (#1, #2, '#3', '#4', #5, '#6');
                    """;
            sql=sql.replace("#1",""+entidade.getGarcon().getId());
            sql=sql.replace("#2",""+entidade.getNumero());
            sql=sql.replace("#3",""+entidade.getData());
            sql=sql.replace("#4",entidade.getDescricao());
            sql=sql.replace("#5",""+entidade.getValor());
            sql=sql.replace("#6",""+entidade.getStatus());
            if (SingletonDB.getConexao().manipular(sql)){
                int id=SingletonDB.getConexao().getMaxPK("comanda","com_id");
                for(Comanda.Item item: entidade.getItens()){
                    sql= """
                            INSERT INTO public.item(
                                com_id, prod_id, it_quantidade, it_valor)
                                VALUES (#1,#2, #3,#4);
                            """;
                    sql=sql.replace("#1",""+id);
                    sql=sql.replace("#2",""+item.produto().getId());
                    sql=sql.replace("#3",""+item.quant());
                    sql=sql.replace("#4",""+item.produto().getPreco());
                    if(!SingletonDB.getConexao().manipular(sql))
                        erro=true;
                }
            }
            else
                erro=true;
            if(!erro)
                SingletonDB.getConexao().getConnect().commit();
            else
                SingletonDB.getConexao().getConnect().rollback();
                SingletonDB.getConexao().getConnect().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return erro;
    }

    @Override
    public boolean alterar(Comanda entidade) {
        boolean erro=false;
        try{
            SingletonDB.getConexao().getConnect().setAutoCommit(false);
            String sql= """
                    UPDATE comanda SET gar_id=#1, com_numero=#2, com_data='#3', com_desc='#4', com_valor=#5, com_status='#6'
                    WHERE com_id=#7
                    """;
            sql=sql.replace("#1",""+entidade.getGarcon().getId());
            sql=sql.replace("#2",""+entidade.getNumero());
            sql=sql.replace("#3",""+entidade.getData());
            sql=sql.replace("#4",entidade.getDescricao());
            sql=sql.replace("#5",(""+entidade.getValor()));
            sql=sql.replace("#6",""+entidade.getStatus());
            sql=sql.replace("#7",""+entidade.getId());
            Comanda comanda= this.get(entidade.getId());
            if (SingletonDB.getConexao().manipular(sql)){
                if(comanda.getItens().size()>0 && !SingletonDB.getConexao().manipular("DELETE FROM item WHERE com_id="+entidade.getId()))
                    erro=true;
                for(Comanda.Item item: entidade.getItens()){
                    sql= """
                            INSERT INTO public.item(
                                com_id, prod_id, it_quantidade, it_valor)
                                VALUES (#1,#2, #3,#4);
                            """;
                    sql=sql.replace("#1",""+entidade.getId());
                    sql=sql.replace("#2",""+item.produto().getId());
                    sql=sql.replace("#3",""+item.quant());
                    sql=sql.replace("#4",""+item.produto().getPreco());
                    if(!SingletonDB.getConexao().manipular(sql))
                        erro=true;
                }
                if(!erro && comanda.getPagamentos().size()>0 && !SingletonDB.getConexao().manipular("DELETE FROM pagamento WHERE com_id="+entidade.getId()))
                    erro=true;
                for(Pagamento pag: entidade.getPagamentos()){
                    sql= """
                            INSERT INTO pagamento(
                                com_id, pag_valor, tpg_id)
                                VALUES (#1,#2, #3);
                            """;
                    sql=sql.replace("#1",""+entidade.getId());
                    sql=sql.replace("#2",""+pag.getValor());
                    sql=sql.replace("#3",""+pag.getTipoPagamento().getId());
                    if(!SingletonDB.getConexao().manipular(sql))
                        erro=true;
                }
            }
            else
                erro=true;
            if(!erro)
                SingletonDB.getConexao().getConnect().commit();
            else{
                System.out.println("O erro foi: "+SingletonDB.getConexao().getMensagemErro());
                SingletonDB.getConexao().getConnect().rollback();
            }
            SingletonDB.getConexao().getConnect().setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return erro;
    }

    @Override
    public boolean apagar(Comanda entidade) {
        boolean ok =false;
        if(SingletonDB.getConexao().manipular("DELETE FROM item WHERE com_id = "+entidade.getId()) && SingletonDB.getConexao().manipular("DELETE FROM pagamento WHERE com_id = "+entidade.getId())){
            SingletonDB.getConexao().manipular("DELETE FROM comanda WHERE com_id = "+entidade.getId());
            ok=true;
        }
        return ok;
    }

    @Override
    public Comanda get(int id) {
        Comanda comanda = null;
        String sql= "SELECT * FROM comanda WHERE com_id="+id;
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try{
            if(resultSet.next()){
                comanda = new Comanda(resultSet.getInt("com_id"),resultSet.getInt("com_numero"),resultSet.getString("com_desc"),resultSet.getDate("com_data").toLocalDate(),resultSet.getDouble("com_valor"),resultSet.getString("com_status").charAt(0), new GarconDAL().get(resultSet.getInt("gar_id")));
                sql="SELECT * FROM item WHERE com_id="+id;
                ResultSet rs2 = SingletonDB.getConexao().consultar(sql);
                while (rs2.next()){
                    Comanda.Item item= new Comanda.Item(new ProdutoDAL().get(rs2.getInt("prod_id")),rs2.getInt("it_quantidade"),rs2.getDouble("it_valor"));
                    comanda.addItem(item);
                }
                sql="SELECT * FROM pagamento WHERE com_id="+id;
                ResultSet rs3 = SingletonDB.getConexao().consultar(sql);
                while (rs3.next()){
                    Pagamento pagamento= new Pagamento((rs3.getInt("pag_id")),rs3.getDouble("pag_valor"),new TipoPagamentoDAL().get(rs3.getInt("tpg_id")));
                    comanda.addPagamento(pagamento.getTipoPagamento(), pagamento.getValor());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return comanda;
    }

    @Override
    public List<Comanda> get(String filtro) {
        List<Comanda> comandas = new ArrayList<>();
        String sql= "SELECT * FROM comanda";
        Comanda comanda=null;
        if(!filtro.isEmpty())
            sql+=" WHERE "+filtro;
        sql+=" ORDER BY com_desc";
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try{
            while(resultSet.next()){
                comanda = new Comanda(resultSet.getInt("com_id"),resultSet.getInt("com_numero"),resultSet.getString("com_desc"),resultSet.getDate("com_data").toLocalDate(),resultSet.getDouble("com_valor"),resultSet.getString("com_status").charAt(0), new GarconDAL().get(resultSet.getInt("gar_id")));
                sql="SELECT * FROM item WHERE com_id="+comanda.getId();
                ResultSet rs2 = SingletonDB.getConexao().consultar(sql);
                while (rs2.next()){
                    Comanda.Item item= new Comanda.Item(new ProdutoDAL().get(rs2.getInt("prod_id")),rs2.getInt("it_quantidade"),rs2.getDouble("it_valor"));
                    comanda.addItem(item);
                }
                sql="SELECT * FROM pagamento WHERE com_id="+comanda.getId();
                ResultSet rs3 = SingletonDB.getConexao().consultar(sql);
                while (rs3.next()){
                    Pagamento pagamento= new Pagamento((rs3.getInt("pag_id")),rs3.getDouble("pag_valor"),new TipoPagamentoDAL().get(rs3.getInt("tpg_id")));
                    comanda.addPagamento(pagamento.getTipoPagamento(), pagamento.getValor());
                }
                comandas.add(comanda);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return comandas;
    }
}
