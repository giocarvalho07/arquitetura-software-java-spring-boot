package com.api_arquitetura_example.api_arquitetura_example.response;

public class ProdutoResponseDTO {

    private Long id;

    private String nome;

    private Double preco;

    public ProdutoResponseDTO() {
    }

    public ProdutoResponseDTO(Long id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
