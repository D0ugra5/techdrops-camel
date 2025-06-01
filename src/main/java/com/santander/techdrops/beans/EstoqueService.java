package com.santander.techdrops.beans;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component("estoqueService")
public class EstoqueService {

    public void verificarEstoque(Exchange exchange) {
        // Simulação de verificação de estoque

        // Você pode acessar os dados do pedido no body (String ou Map, por exemplo)
        String body = exchange.getIn().getBody(String.class);

        // Aqui você pode simular lógica de validação, exemplo:
        if (body.contains("Produto Indisponível")) {
            throw new RuntimeException("Produto fora de estoque (simulado)");
        }

        // Opcional: adicionar headers ou propriedades
        exchange.getIn().setHeader("estoqueVerificado", true);
    }
}