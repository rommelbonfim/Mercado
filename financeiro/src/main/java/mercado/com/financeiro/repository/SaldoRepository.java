package mercado.com.financeiro.repository;

import mercado.com.financeiro.models.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaldoRepository extends JpaRepository<Saldo, Long> {
}
