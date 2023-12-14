package com.infnet.infofinanceira.service;

import java.util.Optional;

import com.infnet.infofinanceira.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	Optional<Usuario> obterPorId(Long id);
	
	void validarEmail(String email);

}