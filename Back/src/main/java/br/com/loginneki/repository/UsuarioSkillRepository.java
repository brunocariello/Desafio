package br.com.loginneki.repository;

import br.com.loginneki.entity.UsuarioSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioSkillRepository extends JpaRepository<UsuarioSkill, Long> {
    Optional<UsuarioSkill> findByUsuarioIdAndSkillId(Long usuarioId, Long skillId);

    boolean existsByUsuarioIdAndSkillId(Long usuarioId, Long skillId);

    List<UsuarioSkill> findByUsuarioId(Long usuarioId);
}
