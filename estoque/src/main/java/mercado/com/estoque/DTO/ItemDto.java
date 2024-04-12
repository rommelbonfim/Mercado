package mercado.com.estoque.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ItemDto {

    private long id;
    private String nome;
    private Integer quantidade;
    private Integer vendidos;
    private BigDecimal valor;
    private String descricao;
    private String fornecedor;
}
