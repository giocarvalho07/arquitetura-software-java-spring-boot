package com.api_arquitetura_example.api_arquitetura_example.mapper;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import org.springframework.stereotype.Component;
import com.api_arquitetura_example.api_arquitetura_example.request.ProdutoRequestDTO;
import com.api_arquitetura_example.api_arquitetura_example.response.ProdutoResponseDTO;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto) {
        if (dto == null) return null;

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        return produto;
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        if (produto == null) return null;

        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        return dto;
    }
}
