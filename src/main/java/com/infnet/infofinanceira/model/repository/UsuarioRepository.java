package com.infnet.infofinanceira.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infnet.infofinanceira.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
	
	@Query( value=" select u.nome from Usuario u where u.id = :idUsuario")
	Optional<Usuario> findByIdUser(@Param("idUsuario") Long idUsuario);
}
