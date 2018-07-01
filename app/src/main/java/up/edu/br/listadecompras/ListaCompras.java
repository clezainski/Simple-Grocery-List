package up.edu.br.listadecompras;

import java.io.Serializable;



public class ListaCompras implements Serializable {
    private Integer id;
    private String produto;
    private String quantidade;
    private String valor;
    private String tipo;
    private String status;


    @Override
    public boolean equals(Object obj) {
        if (id == null || ((ListaCompras)obj).getId() == null){
            return false;
        }
        return id.equals(((ListaCompras)obj).getId());
    }

    @Override
    public int hashCode(){
        return id != null ? id.hashCode() : 0;
    }

    public String toString(){
        return produto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
