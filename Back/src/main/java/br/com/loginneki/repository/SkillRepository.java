package br.com.loginneki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.loginneki.entity.Skill;

@Repository
	public interface SkillRepository extends JpaRepository<Skill, Long> {
	    // Métodos adicionais, se necessário, podem ser definidos aqui
	


}
