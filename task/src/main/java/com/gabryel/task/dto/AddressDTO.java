package com.gabryel.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class AddressDTO {


    @JsonProperty("cep")
    private String zipCode;

    @JsonProperty("logradouro")
    private String street;

    @JsonProperty("complemento")
    private String complement;

    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("uf")
    private String state;

    public AddressDTO() {
    }

    public AddressDTO(AddressDTO.Builder builder) {
        this.zipCode = builder.zipCode;
        this.street = builder.street;
        this.complement = builder.complement;
        this.neighborhood = builder.neighborhood;
        this.city = builder.city;
        this.state = builder.state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getComplement() {
        return complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public static AddressDTO.Builder builder() {
        return new AddressDTO.Builder();
    }

    public static class Builder {

        private String zipCode;
        private String street;
        private String complement;
        private String neighborhood;
        private String city;
        private String state;

        public AddressDTO.Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public AddressDTO.Builder street(String street) {
            this.street = street;
            return this;
        }

        public AddressDTO.Builder complement(String complement) {
            this.complement = complement;
            return this;
        }

        public AddressDTO.Builder neighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public AddressDTO.Builder city(String city) {
            this.city = city;
            return this;
        }

        public AddressDTO.Builder state(String state) {
            this.state = state;
            return this;
        }

        public AddressDTO build() {
            return new AddressDTO(this);
        }
    }

}
