package com.gabryel.task.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Objeto  contendo informações paginadas de um recurso da API.")
public record PagedResponseDTO<T>(
        @Schema(description = "Conteúdo da resposta generico")
        List<T> content,

        @Schema(description = "Número da pagina", example = "0", defaultValue = "0")
        int pageNumber,

        @Schema(description = "Tamanho da pagina", example = "10", defaultValue = "10")
        int pageSize,

        @Schema(description = "Total de elementos", example = "10")
        long totalElements,

        @Schema(description = "Total de paginas", example = "1")
        int totalPages,

        @Schema(description = "Primeira pagina", example = "true")
        boolean isFirst,

        @Schema(description = "Ultima pagina", example = "true")
        boolean isLast
) {
    public PagedResponseDTO() {
        this(null, 0, 10, 0, 0, true, true);
    }
}