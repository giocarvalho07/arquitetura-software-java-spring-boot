package apiErro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor
public class ApiErro {

    private String erro;

    private Integer status;

    private List<String> detalhes;

    private LocalDateTime timestamp;
}


