package up.edu.br.listadecompras;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

//Data Acess Object
public class ListaComprasDAO {

    //Gerencia a ação de salvar ou alterar qualquer dado no banco
    public void salvar (ListaCompras listaCompras){
        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("produto", listaCompras.getProduto());
        values.put("quantidade", listaCompras.getQuantidade());
        values.put("valor", listaCompras.getValor());
        values.put("tipo" , listaCompras.getTipo());
        values.put("status", listaCompras.getStatus());

        if(listaCompras.getId() == null){
            conn.insert("lista_compras", null, values);
        }else{
            conn.update("lista_compras", values, "id = ?",new String[]{listaCompras.getId().toString()});
        }

    }

    //Gerencia a ação de obter dados do banco
    public List<ListaCompras> listar(){
        SQLiteDatabase conn = Conexao.getInstance().getReadableDatabase();

        Cursor c = conn.query("lista_compras", new String[]{"id","produto","quantidade","valor","tipo","status"},
                null,null,null,null,"id");

        ArrayList<ListaCompras> listaCompras = new ArrayList<ListaCompras>();

        if(c.moveToFirst()){
            do{
                ListaCompras listaCompras1 = new ListaCompras();
                listaCompras1.setId(c.getInt(0));
                listaCompras1.setProduto(c.getString(1));
                listaCompras1.setQuantidade(c.getString(2));
                listaCompras1.setValor(c.getString(3));
                listaCompras1.setTipo(c.getString(4));
                listaCompras1.setStatus(c.getString(5));
                listaCompras.add(listaCompras1);
            }while(c.moveToNext());
        }
        return listaCompras;
    }

    ////Gerencia a ação de remover dados do banco
    public void excluir(ListaCompras listaCompras){
        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        conn.delete("lista_compras", "id = ?", new String[] {listaCompras.getId().toString()});
    }

    //Gerencia a ação de alterar somente o status do produto no banco
    public void alterar(ListaCompras listaCompras){
        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", listaCompras.getStatus());

        conn.update("lista_compras", values, "id = ?",new String[]{listaCompras.getId().toString()});
    }

    //Formata valores como Real Brasileiro
    public String setCurrency(Double valor){
        NumberFormat df = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String moneyString = df.format(valor);
        return moneyString;
    }
}
