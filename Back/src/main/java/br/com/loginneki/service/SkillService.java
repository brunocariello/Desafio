package br.com.loginneki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.loginneki.entity.Skill;
import br.com.loginneki.repository.SkillRepository;
import br.com.loginneki.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        logger.debug("Retrieving all Skills");
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        logger.debug("Retrieving Skill by id: {}", id);

        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id " + id));
    }

    public Skill saveSkill(Skill skill) {
        logger.debug("Saving Skill: {}", skill);
        return skillRepository.save(skill);
    }

    public void deleteSkill(Long id) {
        logger.debug("Deleting Skill with id: {}", id);

        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
        } else {
            logger.error("Skill not found with id {}", id);
            throw new ResourceNotFoundException("Skill not found with id " + id);
        }
    }

    // Implementação do método findNameById
    public String findNameById(Long id) {
        return skillRepository.findById(id)
                .map(Skill::getName)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id " + id));
    }
}
