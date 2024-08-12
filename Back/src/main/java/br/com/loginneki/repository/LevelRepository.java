package br.com.loginneki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.loginneki.entity.Level;

public interface LevelRepository extends JpaRepository<Level, Long> {
}
