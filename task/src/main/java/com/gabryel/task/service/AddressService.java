package com.gabryel.task.service;

import com.gabryel.task.client.ViaCepClient;
import com.gabryel.task.dto.AddressDTO;
import com.gabryel.task.exception.CepNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);

    private final ViaCepClient viaCepClient;

    public AddressService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public Mono<AddressDTO> getAddressByCep(final String zipCode) {
        return Mono.just(zipCode)
                .doOnNext( it -> LOGGER.info("Buscando Cep {} " , zipCode))
                .flatMap(viaCepClient::getAddress)
                .onErrorResume(t -> Mono.error(CepNotFoundException::new))
                .retryWhen(Retry
                        .backoff(4, java.time.Duration.ofSeconds(1))
                        .maxBackoff(java.time.Duration.ofSeconds(10))
                        .jitter(0.4)
                        .filter(ex -> ex instanceof CepNotFoundException)
                );
    }

}
