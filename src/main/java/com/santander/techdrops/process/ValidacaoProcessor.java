package com.santander.techdrops.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component("validacaoProcessor")
public class ValidacaoProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Pedido inválido: corpo vazio");
        }
        exchange.getIn().setHeader("produtoId", "123"); // mock de header
    }
}
