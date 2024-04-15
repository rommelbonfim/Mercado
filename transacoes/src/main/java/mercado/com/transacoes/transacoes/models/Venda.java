package mercado.com.transacoes.transacoes.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table(name= "vendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;


    @NotNull
    private long iditem;

    @NotNull
    @PositiveOrZero
    private Integer quantidade;


    @Positive
    private BigDecimal valorvenda;


}
