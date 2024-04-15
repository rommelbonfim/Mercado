package mercado.com.transacoes.transacoes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class EstoqueServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public BigDecimal obterPrecoProdutoPorId(Long iditem) {

        String url = "http://localhost:8082/estoque/Estoque/" + iditem + "/valor";


        return restTemplate.getForObject(url, BigDecimal.class);
    }
}

