package com.santander.techdrops.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

public class StoreRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        rest("/pedidos")
                .post()
                .consumes("application/json")
                .produces("application/json")
                .to("direct:receberPedido");

        from("direct:receberPedido")
                .routeId("rota-pedido-principal")
                .log("Pedido recebido: ${body}")
                .process("validacaoProcessor")
                .to("direct:verificarEstoque")
                .to("direct:consultarFornecedor")
                .multicast()
                .parallelProcessing()
                .to("direct:gravarArquivo", "direct:enviarEmail", "direct:enviarKafka")
                .end()
                .to("direct:salvarPedido");

        from("direct:verificarEstoque")
                .log("Verificando estoque...")
                .toD("sql:SELECT * FROM estoque WHERE produto_id = \'${header.produtoId}\'")
                .choice()
                .when(simple("${body.size} == 0"))
                .throwException(new IllegalArgumentException("Produto sem estoque"))
                .end();

        from("direct:consultarFornecedor")
                .log("Consultando fornecedor...")
                .setHeader("CamelHttpMethod", constant("GET"))
                .to("http://localhost:8081/api/fornecedor");

        from("direct:gravarArquivo")
                .log("Gravando pedido em arquivo de log...")
                .to("file:data/logs");

        from("direct:enviarEmail")
                .log("Enviando confirmação de pedido por e-mail...")
                .to("smtp://smtp.mailtrap.io?username=seu_usuario&password=sua_senha");

        from("direct:enviarKafka")
                .log("Enviando evento para Kafka...")
                .to("kafka:pedidos?brokers=localhost:9092");

        from("direct:salvarPedido")
                .log("Salvando pedido no banco de dados...")
                .to("jpa:com.exemplo.pedido.model.PedidoEntity");
    }
}
