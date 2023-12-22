package com.infnet.infofinanceira.api.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infnet.infofinanceira.api.dto.LancamentoDTO;
import com.infnet.infofinanceira.api.dto.TokenDTO;
import com.infnet.infofinanceira.api.dto.UsuarioDTO;
import com.infnet.infofinanceira.exception.ErroAutenticacao;
import com.infnet.infofinanceira.exception.RegraNegocioException;
import com.infnet.infofinanceira.model.entity.Lancamento;
import com.infnet.infofinanceira.model.entity.Usuario;
import com.infnet.infofinanceira.model.enums.StatusLancamento;
import com.infnet.infofinanceira.model.enums.TipoLancamento;
import com.infnet.infofinanceira.service.JwtService;
import com.infnet.infofinanceira.service.LancamentoService;
import com.infnet.infofinanceira.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioService service;
	private final LancamentoService lancamentoService;
	private final JwtService jwtService;
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar( @RequestBody UsuarioDTO dto ) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			String token = jwtService.gerarToken(usuarioAutenticado);
			TokenDTO tokenDTO = new TokenDTO( usuarioAutenticado.getNome(), token);
			return ResponseEntity.ok(tokenDTO);
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario, "s");
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo( @PathVariable("id") Long id ) {
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity( HttpStatus.NOT_FOUND );
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
	
	@GetMapping("{id}/nome")
	public ResponseEntity obterNome( @PathVariable("id") Long id ) {
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity( HttpStatus.NOT_FOUND );
		}
		
		Optional<Usuario> nome = service.obterNomeDoUsuario(id);
		return ResponseEntity.ok(nome);
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody UsuarioDTO dto ) {
		return service.obterPorId(id).map( entity -> {
			try {
				Usuario usuario = converter(dto);
				usuario.setId(entity.getId());
				service.salvarUsuario(usuario, "a");
				return ResponseEntity.ok(usuario);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () ->
			new ResponseEntity("Usuario não encontrado.", HttpStatus.BAD_REQUEST));
	}
	
	private Usuario converter(UsuarioDTO dto) {
		Usuario usuario = new Usuario();
		
		usuario.setId(dto.getId());
		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(dto.getSenha());
		
		return usuario;
	}
	
	private <T> List<T> converter(Optional<T> optT){
		return optT.map(Collections::singletonList).orElse(Collections.emptyList());
	}
	
	@GetMapping("perfil/{id}")
	public ResponseEntity buscar(@PathVariable("id") Long id) {
		
		Optional<Usuario> usuario = service.obterPorId(id);

		//Class<?> objectClass = usuario.getClass();
	    //String className = objectClass.getSimpleName();
	    
	    List<Usuario> perfil = converter(usuario); 
		
		return ResponseEntity.ok(perfil);
	}
	
	@GetMapping("{id}")
	public ResponseEntity obter( @PathVariable("id") Long id ) {
		return service.obterPorId(id)
					.map( usuario -> new ResponseEntity(converter(usuario), HttpStatus.OK) )
					.orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND) );
	}

	private UsuarioDTO converter(Usuario usuario) {
		return UsuarioDTO.builder()
				.id(usuario.getId())
				.nome(usuario.getNome())
				.email(usuario.getEmail())
				.senha(usuario.getSenha())
				.build();
	}
}
