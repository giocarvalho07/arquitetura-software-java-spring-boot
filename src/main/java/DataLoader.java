import entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // 1. CREATE (Salvar)
        Product p1 = new Product(null, "Smartphone", 2500.0);
        Product p2 = new Product(null, "Laptop", 4500.0);
        Product p3 = new Product(null, "Teclado Mech", 300.0);
        repository.saveAll(Arrays.asList(p1, p2, p3));
        System.out.println(">>> [CREATE] Produtos salvos!");

        // 2. READ (Buscar)
        List<Product> products = repository.findAll();
        System.out.println(">>> [READ] Total de produtos: " + products.size());

        // 3. UPDATE (Atualizar)
        Product toUpdate = repository.findById(1L).get();
        toUpdate.setName("Smartphone Ultra Pro");
        repository.save(toUpdate);
        System.out.println(">>> [UPDATE] Produto ID 1 atualizado!");

        // 4. DELETE (Deletar)
        repository.deleteById(3L);
        System.out.println(">>> [DELETE] Produto ID 3 removido!");

        System.out.println(">>> Setup Inicial Concluído com sucesso!");
    }
}

