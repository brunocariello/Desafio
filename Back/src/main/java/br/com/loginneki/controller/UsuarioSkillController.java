package br.com.loginneki.controller;

import br.com.loginneki.entity.UsuarioSkill;
import br.com.loginneki.exception.ResourceNotFoundException;
import br.com.loginneki.service.UsuarioService;
import br.com.loginneki.service.UsuarioSkillService;
import br.com.loginneki.util.JwtUtil;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario-skill")
public class UsuarioSkillController {

    private final UsuarioSkillService usuarioSkillService;
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioSkillController.class);

    @Autowired
    public UsuarioSkillController(UsuarioSkillService usuarioSkillService, UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioSkillService = usuarioSkillService;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createUsuarioSkill(@RequestBody UsuarioSkill usuarioSkill) {
        if (usuarioSkill.getUsuarioId() == null) {
            return new ResponseEntity<>("usuarioId must not be null", HttpStatus.BAD_REQUEST);
        }
        UsuarioSkill createdUsuarioSkill = usuarioSkillService.saveUsuarioSkill(usuarioSkill);
        return new ResponseEntity<>(createdUsuarioSkill, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuarioSkill(@PathVariable Long id, @RequestBody UsuarioSkill usuarioSkill) {
        if (usuarioSkill.getUsuarioId() == null) {
            return new ResponseEntity<>("usuarioId must not be null", HttpStatus.BAD_REQUEST);
        }
        try {
            UsuarioSkill updatedUsuarioSkill = usuarioSkillService.updateUsuarioSkill(id, usuarioSkill);
            return new ResponseEntity<>(updatedUsuarioSkill, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/update-level")
    public ResponseEntity<?> updateSkillLevel(@RequestParam("usuarioId") Long usuarioId,
                                              @RequestParam("skillId") Long skillId,
                                              @RequestParam("levelId") Long levelId) {
        if (usuarioId == null || skillId == null || levelId == null) {
            return new ResponseEntity<>("usuarioId, skillId, and levelId must not be null", HttpStatus.BAD_REQUEST);
        }
        try {
            UsuarioSkill updatedUsuarioSkill = usuarioSkillService.updateSkillLevel(usuarioId, skillId, levelId);
            return new ResponseEntity<>(updatedUsuarioSkill, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioSkillById(@PathVariable Long id) {
        try {
            UsuarioSkill usuarioSkill = usuarioSkillService.getUsuarioSkillById(id);
            return new ResponseEntity<>(usuarioSkill, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<?> deleteUsuarioSkillByTokenAndSkillId(@RequestHeader("Authorization") String token,
                                                                 @PathVariable Long skillId) {
        try {
            logger.debug("Deleting UsuarioSkill with skillId: {} from token: {}", skillId, token);

            // Extrai o email do token
            String email = jwtUtil.extractUsername(token);
            logger.debug("Extracted email from token: {}", email);

            // Obtém o usuarioId com base no email
            Long usuarioId = usuarioSkillService.getUsuarioIdByEmail(email);
            logger.debug("Retrieved usuarioId for email {}: {}", email, usuarioId);

            // Tenta deletar a associação usando o usuarioId e skillId
            if (usuarioSkillService.deleteByUserIdAndSkillId(usuarioId, skillId)) {
                logger.info("Successfully deleted UsuarioSkill with usuarioId: {} and skillId: {}", usuarioId, skillId);
                return ResponseEntity.noContent().build();
            } else {
                // Retorna NOT_FOUND se a associação não existir
                logger.warn("UsuarioSkill not found for usuarioId: {} and skillId: {}", usuarioId, skillId);
                return ResponseEntity.notFound().build();
            }
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An error occurred while deleting the skill: {}", e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while deleting the skill", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-usuario/{usuarioId}")
    public ResponseEntity<?> getSkillsByUsuarioId(@PathVariable Long usuarioId) {
        if (usuarioId == null) {
            return new ResponseEntity<>("usuarioId must not be null", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(usuarioSkillService.getSkillsByUsuarioId(usuarioId), HttpStatus.OK);
    }

    @GetMapping("/valid-id")
    public ResponseEntity<?> getValidUsuarioSkillId() {
        try {
            List<Long> validIds = usuarioSkillService.getValidUsuarioSkillIds();
            if (validIds.isEmpty()) {
                return new ResponseEntity<>("No valid IDs found", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(validIds, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching valid IDs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-skills")
    public ResponseEntity<?> getSkillsForAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            Long usuarioId = usuarioSkillService.getUsuarioIdByEmail(email);

            if (usuarioId == null) {
                return new ResponseEntity<>("User ID not found for the authenticated user", HttpStatus.NOT_FOUND);
            }

            List<UsuarioSkill> usuarioSkills = usuarioSkillService.getSkillsByUsuarioId(usuarioId);
            return new ResponseEntity<>(usuarioSkills, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the user's skills", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
