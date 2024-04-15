package mercado.com.estoque.DTO;


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
