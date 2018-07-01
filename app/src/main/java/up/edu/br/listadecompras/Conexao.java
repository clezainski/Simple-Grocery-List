package up.edu.br.listadecompras;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    private static Conexao conexao;
    public static Conexao getInstance(){
        return conexao;
    }

    //Faz  a conexão e criação do banco em si
    public Conexao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ) {
        super(context, name, factory,version);
        conexao = this;
    }

    //cria as tabelas do banco
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String statement = "create table lista_compras ("+
                "id integer primary key autoincrement," +
                "produto varchar(255)," +
                "quantidade varchar(50)," +
                "valor varchar(20)," +
                "tipo varchar(20)," +
                "status varchar(50)" +
                ")";

        sqLiteDatabase.execSQL(statement);
    }

    //Atualiza tabelas
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String statement = "alter table lista_compras" + "add teste varchar(255);";
        sqLiteDatabase.execSQL(statement);
    }
}
