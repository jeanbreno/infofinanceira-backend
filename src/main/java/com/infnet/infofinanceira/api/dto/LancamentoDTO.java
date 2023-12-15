package com.infnet.infofinanceira.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO {
	
	private Long id;
	private Long usuario;
	private String descricao;
	private String tipo;
	private String status;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	
}
