package mercado.com.transacoes.transacoes.repository;

import mercado.com.transacoes.transacoes.models.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
