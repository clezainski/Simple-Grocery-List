package up.edu.br.listadecompras;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ProdutoActivity extends AppCompatActivity {

    ListaCompras listaCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        EditText txtProduto = (EditText) findViewById(R.id.txtProduto);
        Spinner spTipo = (Spinner) findViewById(R.id.spTipo);
        EditText txtQuantidade = (EditText) findViewById(R.id.txtQuantidade);
        EditText txtValor = (EditText) findViewById(R.id.txtValor);

        Intent it = getIntent();
        if (it != null && it.hasExtra("lista_compras")) {
            listaCompras = (ListaCompras) it.getSerializableExtra("lista_compras");

            txtProduto.setText(listaCompras.getProduto());
            txtQuantidade.setText(listaCompras.getQuantidade());
            txtValor.setText(listaCompras.getValor());

            spTipo.setSelection(((ArrayAdapter) spTipo.getAdapter()).getPosition(listaCompras.getTipo()));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_produto, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            EditText txtProduto = (EditText)findViewById(R.id.txtProduto);
            EditText txtQuantidade = (EditText)findViewById(R.id.txtQuantidade);
            EditText txtValor = (EditText)findViewById(R.id.txtValor);
            Spinner spTipo = (Spinner)findViewById(R.id.spTipo);

            boolean empty = false;

            if(listaCompras == null){
                listaCompras = new ListaCompras();
                empty = true;
            }

            listaCompras.setProduto(txtProduto.getText().toString());
            listaCompras.setTipo(spTipo.getSelectedItem().toString());
            listaCompras.setQuantidade(txtQuantidade.getText().toString());
            listaCompras.setValor(txtValor.getText().toString());
            if (empty == true)listaCompras.setStatus("1");

            //Checa por campos inválidos e faz as devidas notificações ou correções
            if (listaCompras.getProduto().isEmpty() == true){
                Toast.makeText(getApplicationContext(), "Nome do produto requerido!",Toast.LENGTH_LONG).show();
            }
            else{
                if (listaCompras.getQuantidade().isEmpty() == true){
                    listaCompras.setQuantidade("1");
                }
                if (listaCompras.getValor().isEmpty() == true){
                    listaCompras.setValor("0");
                }
                new ListaComprasDAO().salvar(listaCompras);
                Toast.makeText(getApplicationContext(), "Salvo com sucesso!",Toast.LENGTH_LONG).show();
                listaCompras = null;

                Intent it = new Intent(ProdutoActivity.this,MainActivity.class);
                startActivity(it);
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }



}
