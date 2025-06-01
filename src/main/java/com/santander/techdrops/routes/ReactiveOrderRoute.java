package com.santander.techdrops.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ReactiveOrderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        System.out.println(">>> Rota reativa foi registrada");

        from("timer:gerador-pedidos?delay=5000&period=5000")
                .routeId("emissao-pedido-reativo")
                .setBody()
                .simple("{\"cliente\":\"Jean Timer\",\"produto\":\"Produto Timer\",\"quantidade\":1,\"valorTotal\":100.0}")
                .to("reactive-streams:novos-pedidos");

        from("reactive-streams:novos-pedidos")
                .routeId("rota-pedido-reativo")
                .log(">>> Rota de consumo do stream registrada")
                .log("[Reativo] Novo pedido recebido: ${body}")
                .process("validacaoProcessor")
                .to("bean:estoqueService?method=verificarEstoque")
                .process(exchange -> {
                    throw new RuntimeException("Erro durante o processamento!");
                })
                .to("http://localhost:8081/api/fornecedor")
                .to("kafka:pedidos-reativos?brokers=localhost:9092")
                .to("log:pedido-reativo-finalizado");
    }
}
