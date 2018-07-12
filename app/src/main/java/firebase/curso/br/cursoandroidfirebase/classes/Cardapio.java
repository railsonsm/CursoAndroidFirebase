package firebase.curso.br.cursoandroidfirebase.classes;

public class Cardapio {
    private String keyProduto;
    private String descricao;
    private String nomePrato;
    private String serveQuantidade;
    private String urlImagem;
    private String preco;

    public String getKeyProduto() {
        return keyProduto;
    }

    public void setKeyProduto(String keyProduto) {
        this.keyProduto = keyProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomePrato() {
        return nomePrato;
    }

    public void setNomePrato(String nomePrato) {
        this.nomePrato = nomePrato;
    }

    public String getServeQuantidade() {
        return serveQuantidade;
    }

    public void setServeQuantidade(String serveQuantidade) {
        this.serveQuantidade = serveQuantidade;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
