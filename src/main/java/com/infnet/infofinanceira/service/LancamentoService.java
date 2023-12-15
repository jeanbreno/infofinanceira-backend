package com.infnet.infofinanceira.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.infnet.infofinanceira.model.entity.Lancamento;
import com.infnet.infofinanceira.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void deletar(Lancamento lancamento);
	
	BigDecimal obterSaldoPorUsuario(Long id);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);

	Optional<Lancamento> obterPorId(Long id);

}
