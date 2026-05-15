package mapper;

import entity.Produto;
import org.springframework.stereotype.Component;
import request.ProdutoRequestDTO;
import response.ProdutoResponseDTO;

@Component
public class ProdutoMapper {

    /**
     * Converte os dados recebidos na requisição para a Entidade JPA.
     * O ID é passado como null pois será gerado pelo banco.
     */
    public Produto toEntity(ProdutoRequestDTO dto) {
        if (dto == null) return null;

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        return produto;
    }

    /**
     * Converte a Entidade JPA para o DTO de Resposta.
     * Protege campos sensíveis ou internos da entidade.
     */
    public ProdutoResponseDTO toResponse(Produto produto) {
        if (produto == null) return null;

        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        return dto;
    }
}
