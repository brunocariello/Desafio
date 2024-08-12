package br.com.loginneki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.loginneki.entity.Level;
import br.com.loginneki.repository.LevelRepository;
import br.com.loginneki.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class LevelService {

    @Autowired
    private LevelRepository levelRepository;

    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    public Optional<Level> getLevelById(Long id) {
        return levelRepository.findById(id);
    }

    public Level saveLevel(Level level) {
        return levelRepository.save(level);
    }

    public void deleteLevel(Long id) {
        levelRepository.deleteById(id);
    }

    // Implementação do método findNameById
    public String findNameById(Long id) {
        return levelRepository.findById(id)
                .map(Level::getNome)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found with id " + id));
    }
}
