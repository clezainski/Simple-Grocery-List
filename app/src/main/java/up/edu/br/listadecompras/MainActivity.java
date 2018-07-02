package up.edu.br.listadecompras;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    int opt;

    //carrega ao iniciar a activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //paramentos de conexão e criação do banco de dados
        new Conexao(getApplicationContext(), "compras.db", null, 1);

        //carrega o float menu
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //abre a intent de cadastro de novos produtos
                Intent it = new Intent(MainActivity.this, ProdutoActivity.class);
                startActivity(it);
            }
        });

        //encontra a view associada ao componente ListView Lista Produtos
        ListView listaListaDeCompras = (ListView) findViewById(R.id.listaProdutos);

        //ViewAdapter do ArrayList ListaCompras
        ListaComprasAdapter listaComprasAdapter = new ListaComprasAdapter(new ListaComprasDAO().listar(),
                this);
        listaListaDeCompras.setAdapter(listaComprasAdapter);

        //Ação ao clicar no ViewAdapter
        listaListaDeCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int posicao, long l) {

                //Dialogo para escolha de alterar status da compra ou infomações do pedido
                new AlertDialog.Builder(MainActivity.this);
                AlertDialog.Builder alert =
                        new AlertDialog.Builder
                                (MainActivity.this);
                alert.setMessage("O que você deseja alterar?");
                alert.setCancelable(true);
                //Altera o status do produto no banco
                alert.setPositiveButton("Status", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Obtem o item selecionado
                                ListaCompras listaCompras = (ListaCompras) adapterView.
                                        getItemAtPosition(posicao);

                                //Altera do status do produto
                                if (listaCompras.getStatus().equals("0"))
                                    listaCompras.setStatus("1");
                                else listaCompras.setStatus("0");

                                new ListaComprasDAO().alterar(listaCompras);
                                ((ListaComprasAdapter) adapterView.getAdapter()).notifyDataSetChanged();

                                //Checa se o produto que teve o status alterado foi marcado como comprado
                                if (listaCompras.getStatus().equals("0")) {
                                    Toast.makeText(getApplicationContext(), listaCompras.getProduto() +
                                            " comprado!", Toast.LENGTH_LONG).show();

                                    //Ao marcar um produto como comprado, verica se há algum outro
                                    //produto pendente no banco, para ser comprado
                                    List<ListaCompras> listaCompras1 = new ListaComprasDAO().listar();
                                    boolean finalizado = true;
                                    Double total = 0.0;
                                    for(ListaCompras c: listaCompras1){
                                        if (c.getStatus().equals("1")){
                                            finalizado = false;
                                        }
                                        total += Double.parseDouble(c.getQuantidade()) *
                                                Double.parseDouble(c.getValor());
                                    }
                                    //Chama a notificação, caso tudo tenha sido comprado
                                    if (finalizado==true){

                                        Notifica("Você finalizou sua compra! Você gastou " +
                                                new ListaComprasDAO().setCurrency(total) );
                                    }
                                }
                                //devolve um produto comprado
                                else
                                    Toast.makeText(getApplicationContext(), listaCompras.getProduto() +
                                            " devolvido!", Toast.LENGTH_LONG).show();
                            }

                        });
                //chama a activity de cadastro enviando um ArrayList como parâmetro
                alert.setNegativeButton("Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListaCompras c = (ListaCompras) adapterView.getItemAtPosition(posicao);
                        Intent it = new Intent(MainActivity.this, ProdutoActivity.class);
                        it.putExtra("lista_compras", c);
                        startActivity(it);
                    }
                });
                alert.show();
            }
        });

        //Ação ao manter o clique sobre a ViewAdapter
        listaListaDeCompras.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView,
                                           View view, final int posicao, long l) {

                //Questiona se deseja realmente excluir
                new AlertDialog.Builder(MainActivity.this);
                AlertDialog.Builder alert =
                        new AlertDialog.Builder
                                (MainActivity.this);
                alert.setMessage("Deseja realmente excluir?");
                alert.setCancelable(false);
                alert.setPositiveButton("Sim", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ListaCompras listaCompras = (ListaCompras) adapterView.
                                        getItemAtPosition(posicao);

                                //remove somente o registro selecionado do banco e atualiza a view
                                new ListaComprasDAO().excluir(listaCompras);
                                ((ListaComprasAdapter) adapterView.getAdapter()).remove(listaCompras);
                                Toast.makeText(getApplicationContext(), listaCompras.getProduto() +
                                        " excluído!", Toast.LENGTH_LONG).show();

                            }
                        });
                //caso negativo, canncela a ação
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alert.show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Obtem o ArrayList do banco
        List<ListaCompras> listaCompras = new ListaComprasDAO().listar();

        //Variaveis auxiliares
        String texto = "";
        Double valorTotal = 0.0;

        //Percorre o ArrayList, calcula valores e popula uma variavel tipo string
        for (ListaCompras c : listaCompras) {
            texto += "Produto: " + c.getProduto() + System.getProperty("line.separator");
            texto += "Quantidade: " + c.getQuantidade() + System.getProperty("line.separator");
            texto += "Valor unitário: " + new ListaComprasDAO().setCurrency(Double.parseDouble(c.getValor()))
                    + System.getProperty("line.separator");
            texto += "Categoria: " + c.getTipo() + System.getProperty("line.separator");
            if (c.getStatus().equals("1"))
                texto += "Status do produto: Não comprado" + System.getProperty("line.separator");
            else texto += "Status do produto: Comprado" + System.getProperty("line.separator");
            texto += "" + System.getProperty("line.separator");
            valorTotal += Double.parseDouble(c.getValor()) * Double.parseDouble(c.getQuantidade());
        }
        texto += "" + System.getProperty("line.separator");
        texto += "Valor total da lista de compra: " + new ListaComprasDAO().setCurrency(valorTotal) +
                System.getProperty("line.separator");

        //noinspection SimplifiableIfStatement
        if (id == R.id.email) {

            //Intent Filter responsável por solicitar o envio do e-mail
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Minha lista de compras");
            emailIntent.putExtra(Intent.EXTRA_TEXT, texto);
            startActivity(Intent.createChooser(emailIntent, "Enviar por e-mail..."));

            return true;
        }

        if (id == R.id.whats) {

            //Intent Filter responsável por solicitar o envio do e-mail
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Envia um noticação via Central de Notificações do Android
    private void Notifica(String mensagem){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.rounded_ico)
                        .setContentTitle("Lista de Compras")
                        .setContentText(mensagem)
                        .setVibrate(new long[] { 100, 250 });
        // Cria o intent que irá chamar a atividade a ser aberta quando clicar na notifição
        Intent resultIntent = new Intent(MainActivity.this, MainActivity.class);
        //PendingIntent é "vinculada" a uma notification para abrir a intent
        PendingIntent resultPendingIntent = PendingIntent.
                getActivity(MainActivity.this, 0, resultIntent, 0);

        //associa o intent na notificação
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //gera a notificação
        mNotificationManager.notify(99, mBuilder.build());
    }
}
