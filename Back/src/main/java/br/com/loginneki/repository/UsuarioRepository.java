package br.com.loginneki.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.loginneki.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
}
