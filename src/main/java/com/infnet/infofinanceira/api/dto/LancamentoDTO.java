package com.infnet.infofinanceira.api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
