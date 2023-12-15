package com.infnet.infofinanceira.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infnet.infofinanceira.exception.ErroAutenticacao;
import com.infnet.infofinanceira.exception.RegraNegocioException;
import com.infnet.infofinanceira.model.entity.Usuario;
import com.infnet.infofinanceira.model.repository.UsuarioRepository;
import com.infnet.infofinanceira.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	private UsuarioRepository repository;
	private PasswordEncoder encoder;
	
	public UsuarioServiceImpl(
			UsuarioRepository repository, 
			PasswordEncoder encoder) {
		super();
		this.repository = repository;
		this.encoder = encoder;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Credenciais inválidas.");
		}
		
		boolean senhasBatem = encoder.matches(senha, usuario.get().getSenha());
		
		if(!senhasBatem) {
			throw new ErroAutenticacao("Credenciais inválidas.");
		}

		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		criptografarSenha(usuario);
		return repository.save(usuario);
	}

	private void criptografarSenha(Usuario usuario) {
		String senha = usuario.getSenha();
		String senhaCripto = encoder.encode(senha);
		usuario.setSenha(senhaCripto);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Email já cadastrado.");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		Optional<Usuario> result = repository.findById(id);
		
		return result;
	}

	@Override
	@Transactional
	public Optional<Usuario> obterNomeDoUsuario(Long id) {
		return repository.findByIdUser(id);
	}

}