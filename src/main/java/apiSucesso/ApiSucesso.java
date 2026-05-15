package apiSucesso;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class ApiSucesso<T> {

    private String mensagem;

    private T dados;

    private LocalDateTime timestamp;

    public ApiSucesso(String mensagem, T dados) {
        this.mensagem = mensagem;
        this.dados = dados;
        this.timestamp = LocalDateTime.now();
    }
}
