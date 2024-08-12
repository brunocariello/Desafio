package br.com.loginneki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.loginneki.entity.Level;
import br.com.loginneki.service.LevelService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/levels")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @GetMapping
    public ResponseEntity<List<Level>> getAllLevels() {
        List<Level> levels = levelService.getAllLevels();
        return ResponseEntity.ok(levels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Level> getLevelById(@PathVariable Long id) {
        Optional<Level> level = levelService.getLevelById(id);
        return level.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Level> createLevel(@RequestBody Level level) {
        Level createdLevel = levelService.saveLevel(level);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLevel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Level> updateLevel(@PathVariable Long id, @RequestBody Level level) {
        if (levelService.getLevelById(id).isPresent()) {
            level.setId(id);
            Level updatedLevel = levelService.saveLevel(level);
            return ResponseEntity.ok(updatedLevel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        if (levelService.getLevelById(id).isPresent()) {
            levelService.deleteLevel(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
