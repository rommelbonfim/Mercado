package mercado.com.estoque.repository;

import mercado.com.estoque.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByNome(String nome);
}
