package br.com.loginneki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.loginneki.entity.UsuarioSkill;
import br.com.loginneki.repository.UsuarioSkillRepository;
import br.com.loginneki.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioSkillService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioSkillService.class);

    private final UsuarioSkillRepository usuarioSkillRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioSkillService(UsuarioSkillRepository usuarioSkillRepository, UsuarioService usuarioService) {
        this.usuarioSkillRepository = usuarioSkillRepository;
        this.usuarioService = usuarioService;
    }

    public UsuarioSkill saveUsuarioSkill(UsuarioSkill usuarioSkill) {
        logger.debug("Saving UsuarioSkill: {}", usuarioSkill);
        return usuarioSkillRepository.save(usuarioSkill);
    }

    public UsuarioSkill updateUsuarioSkill(Long id, UsuarioSkill usuarioSkill) {
        logger.debug("Updating UsuarioSkill with id: {}", id);

        if (usuarioSkillRepository.existsById(id)) {
            usuarioSkill.setId(id);
            return usuarioSkillRepository.save(usuarioSkill);
        } else {
            logger.error("UsuarioSkill not found with id {}", id);
            throw new ResourceNotFoundException("UsuarioSkill not found with id " + id);
        }
    }

    public UsuarioSkill getUsuarioSkillById(Long id) {
        logger.debug("Retrieving UsuarioSkill by id: {}", id);
        Optional<UsuarioSkill> usuarioSkill = usuarioSkillRepository.findById(id);
        if (usuarioSkill.isPresent()) {
            return usuarioSkill.get();
        } else {
            logger.error("UsuarioSkill not found with id {}", id);
            throw new ResourceNotFoundException("UsuarioSkill not found with id " + id);
        }
    }

    public void deleteUsuarioSkill(Long id) {
        logger.debug("Deleting UsuarioSkill with id: {}", id);

        if (usuarioSkillRepository.existsById(id)) {
            usuarioSkillRepository.deleteById(id);
        } else {
            logger.error("UsuarioSkill not found with id {}", id);
            throw new ResourceNotFoundException("UsuarioSkill not found with id " + id);
        }
    }

    public boolean deleteByUserIdAndSkillId(Long usuarioId, Long skillId) {
        logger.debug("Deleting UsuarioSkill with usuarioId: {} and skillId: {}", usuarioId, skillId);

        Optional<UsuarioSkill> usuarioSkill = usuarioSkillRepository.findByUsuarioIdAndSkillId(usuarioId, skillId);
        if (usuarioSkill.isPresent()) {
            usuarioSkillRepository.delete(usuarioSkill.get());
            return true;
        } else {
            logger.warn("UsuarioSkill not found with usuarioId: {} and skillId: {}", usuarioId, skillId);
            return false;
        }
    }

    public boolean existsByUserIdAndSkillId(Long usuarioId, Long skillId) {
        logger.debug("Checking existence of UsuarioSkill with usuarioId: {} and skillId: {}", usuarioId, skillId);
        return usuarioSkillRepository.existsByUsuarioIdAndSkillId(usuarioId, skillId);
    }

    public List<UsuarioSkill> getSkillsByUsuarioId(Long usuarioId) {
        logger.debug("Retrieving skills by usuarioId: {}", usuarioId);
        return usuarioSkillRepository.findByUsuarioId(usuarioId);
    }

    public Long getUsuarioIdByEmail(String email) {
        logger.debug("Retrieving usuarioId by email: {}", email);
        Long usuarioId = usuarioService.getUsuarioIdByEmail(email);
        if (usuarioId != null) {
            return usuarioId;
        } else {
            logger.error("Usuario not found with email {}", email);
            throw new ResourceNotFoundException("Usuario not found with email " + email);
        }
    }

    public List<Long> getValidUsuarioSkillIds() {
        logger.debug("Retrieving valid UsuarioSkill IDs");
        return usuarioSkillRepository.findAll().stream()
                .map(UsuarioSkill::getId)
                .collect(Collectors.toList());
    }

    public UsuarioSkill updateSkillLevel(Long usuarioId, Long skillId, Long levelId) {
        logger.debug("Updating skill level for usuarioId: {}, skillId: {}, levelId: {}", usuarioId, skillId, levelId);

        Optional<UsuarioSkill> usuarioSkillOpt = usuarioSkillRepository.findByUsuarioIdAndSkillId(usuarioId, skillId);
        if (usuarioSkillOpt.isPresent()) {
            UsuarioSkill usuarioSkill = usuarioSkillOpt.get();
            usuarioSkill.setLevelId(levelId);
            return usuarioSkillRepository.save(usuarioSkill);
        } else {
            logger.error("UsuarioSkill not found with usuarioId {} and skillId {}", usuarioId, skillId);
            throw new ResourceNotFoundException("UsuarioSkill not found with usuarioId " + usuarioId + " and skillId " + skillId);
        }
    }
}
