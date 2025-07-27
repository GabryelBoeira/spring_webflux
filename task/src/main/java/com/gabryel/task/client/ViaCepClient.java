package com.gabryel.task.client;

import com.gabryel.task.dto.AddressDTO;
import com.gabryel.task.exception.CepNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ViaCepClient {

    private static final String VIA_CEP_URI = "/{cep}/json";

    private final WebClient client;

    public ViaCepClient(WebClient viaCep) {
        this.client = viaCep;
    }

    public Mono<AddressDTO> getAddress(final String zipCode) {
        return client
                .get()
                .uri(VIA_CEP_URI, zipCode)
                .retrieve()
                .bodyToMono(AddressDTO.class)
                .onErrorResume(ex -> Mono.error(new CepNotFoundException()));
    }

}
