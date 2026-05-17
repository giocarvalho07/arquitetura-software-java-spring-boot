package com.api_arquitetura_example.api_arquitetura_example;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.api_arquitetura_example.api_arquitetura_example.service.ProdutoService;

@Component
public class Dataloader implements CommandLineRunner {

    @Autowired
    private ProdutoService service;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n>>> [LOADER] Iniciando testes de ciclo de vida do CRUD...");

        // 1. Teste de Criação (CREATE)
        Produto p1 = service.salvar(new Produto(null, "Teclado Mecânico RGB", 350.0));
        Produto p2 = service.salvar(new Produto(null, "Mouse Wireless Pro", 220.0));
        System.out.println(">>> [LOADER] Produtos inseridos: " + p1.getNome() + ", " + p2.getNome());

        // 2. Teste de Consulta (READ)
        Produto buscado = service.buscarPorId(p1.getId());
        if (buscado != null) {
            System.out.println(">>> [LOADER] Produto recuperado com sucesso: " + buscado.getNome());

            // 3. Teste de Atualização (UPDATE)
            buscado.setNome("Teclado Mecânico Custom");
            buscado.setPreco(450.0);
            service.salvar(buscado);
            System.out.println(">>> [LOADER] Produto ID " + buscado.getId() + " atualizado.");
        }

        // 4. Teste de Deleção (DELETE)
        if (p2 != null && p2.getId() != null) {
            Long idParaDeletar = p2.getId();
            service.deletar(idParaDeletar);
            System.out.println(">>> [LOADER] Produto ID " + idParaDeletar + " removido com sucesso.");
        }

        // Carga Final: Dados para teste manual via Postman
        service.salvar(new Produto(null, "Monitor UltraWide 34'", 2800.0));
        service.salvar(new Produto(null, "Headset Gamer 7.1", 450.0));

        System.out.println(">>> [LOADER] Ciclo de vida validado. Banco pronto para uso.\n");
    }
}
