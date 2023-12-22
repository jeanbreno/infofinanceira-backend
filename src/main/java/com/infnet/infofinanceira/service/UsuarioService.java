package com.infnet.infofinanceira.service;

import java.util.List;
import java.util.Optional;

import com.infnet.infofinanceira.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario, String req);
	
	Optional<Usuario> obterPorId(Long id);
	
	Optional<Usuario> obterNomeDoUsuario(Long id);
	
	void validarEmail(String email);
	
	Usuario atualizar(Usuario usuario);
	
	Optional<Usuario> buscar(Long id);

}