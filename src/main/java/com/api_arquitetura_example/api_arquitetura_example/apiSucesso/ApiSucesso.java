package com.api_arquitetura_example.api_arquitetura_example.apiSucesso;

import java.time.LocalDateTime;

public class ApiSucesso<T> {

    private String mensagem;

    private T dados;

    private LocalDateTime timestamp;

    public ApiSucesso(String mensagem, T dados) {
        this.mensagem = mensagem;
        this.dados = dados;
        this.timestamp = LocalDateTime.now();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public T getDados() {
        return dados;
    }

    public void setDados(T dados) {
        this.dados = dados;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
