package com.api_arquitetura_example.api_arquitetura_example.apiErro;

import java.time.LocalDateTime;
import java.util.List;

public class ApiErro {

    private String erro;

    private Integer status;

    private List<String> detalhes;

    private LocalDateTime timestamp;

    public ApiErro(String erro, Integer status, List<String> detalhes, LocalDateTime timestamp) {
        this.erro = erro;
        this.status = status;
        this.detalhes = detalhes;
        this.timestamp = timestamp;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(List<String> detalhes) {
        this.detalhes = detalhes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
