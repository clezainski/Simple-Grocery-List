package up.edu.br.listadecompras;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

public class ListaComprasAdapter extends BaseAdapter {

    private List<ListaCompras> listaCompras;
    Activity act;


    public ListaComprasAdapter(List<ListaCompras> listaCompras,Activity act){
        this.listaCompras = listaCompras;
        this.act = act;
    }

    @Override
    public int getCount() {
        return this.listaCompras.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listaCompras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //Carrega a ListView
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = act.getLayoutInflater().inflate(R.layout.activity_lista_compras_adapter, viewGroup, false);

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        TextView textProduto = (TextView) v.findViewById(R.id.adpProduto);
        TextView textValor = (TextView) v.findViewById(R.id.adpValor);
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.llhBackgroung);
        TextView txtView1 = (TextView)v.findViewById(R.id.txtStatus);

        ListaCompras c = listaCompras.get(i);

        textProduto.setText(c.getQuantidade() + "   " + c.getProduto());
        Double total = Double.parseDouble(c.getQuantidade()) * Double.parseDouble(c.getValor());

        String valorTotal = new ListaComprasDAO().setCurrency(total);

        textValor.setText("Total: " + valorTotal);

        if (c.getStatus().equals("0")){
            txtView1.setBackgroundColor(Color.parseColor("#15ff00"));
            txtView1.setText("Comprado");
        }
        else{
            txtView1.setBackgroundColor(Color.parseColor("#ff3232"));
            txtView1.setText("Não comprado");
            txtView1.setTextColor(Color.parseColor("#d6d6d6"));
        }

        if(c.getTipo().equals("Ferramentas")){
            imageView.setImageResource(R.drawable.tools);
        }
        if(c.getTipo().equals("Roupas")){
            imageView.setImageResource(R.drawable.clothes);
        }
        if (c.getTipo().equals("Alimentos")){
            imageView.setImageResource(R.drawable.food);
        }
        if (c.getTipo().equals("Artigos Escolares")){
            imageView.setImageResource(R.drawable.school);
        }

        return v;
    }

    public void remove (ListaCompras compras) {
        listaCompras.remove(compras);
        notifyDataSetChanged();
    }
}
