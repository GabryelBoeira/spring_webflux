package com.gabryel.task.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AddressEntity {

    @Id
    private String id;

    private String zipCode;

    private String street;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;

    public AddressEntity() {
    }

    public AddressEntity(Builder builder) {
        this.id = builder.id;
        this.zipCode = builder.zipCode;
        this.street = builder.street;
        this.complement = builder.complement;
        this.neighborhood = builder.neighborhood;
        this.city = builder.city;
        this.state = builder.state;
    }

    public String getId() {
        return id;
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

    public static Builder builder() {
        return new Builder();
    }

    public AddressEntity.Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .zipCode(this.zipCode)
                .street(this.street)
                .complement(this.complement)
                .neighborhood(this.neighborhood)
                .city(this.city)
                .state(this.state);
    }

    public static class Builder {

        private String id;
        private String zipCode;
        private String street;
        private String complement;
        private String neighborhood;
        private String city;
        private String state;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder complement(String complement) {
            this.complement = complement;
            return this;
        }

        public Builder neighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public AddressEntity build() {
            return new AddressEntity(this);
        }
    }

}
