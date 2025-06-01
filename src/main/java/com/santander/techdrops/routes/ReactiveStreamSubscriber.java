package com.santander.techdrops.routes;

import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;

@Component
public class ReactiveStreamSubscriber {

    private final CamelContext camelContext;

    public ReactiveStreamSubscriber(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void init() {
        CamelReactiveStreamsService reactive = CamelReactiveStreams.get(camelContext);
        reactive.fromStream("novos-pedidos", String.class).subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                System.out.println(">>> [Subscriber] Payload recebido diretamente: " + s);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(">>> [Subscriber] Erro: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println(">>> [Subscriber] Concluído");
            }
        });
    }
}