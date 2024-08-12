package br.com.loginneki.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.loginneki.entity.UsuarioSkill;
import br.com.loginneki.service.SkillService;
import br.com.loginneki.service.LevelService;
import br.com.loginneki.service.UsuarioSkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import br.com.loginneki.entity.Usuario;
import br.com.loginneki.service.UsuarioService;
import br.com.loginneki.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioSkillService usuarioSkillService;  // Injeção do serviço de skills

    @Autowired
    private SkillService skillService;  // Injeção do serviço de skills para obter nomes

    @Autowired
    private LevelService levelService;  // Injeção do serviço de níveis para obter nomes

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String senha = loginRequest.getSenha();

        logger.debug("Tentando logar com email: {}", email);

        Optional<Usuario> optionalUsuario = usuarioService.buscarPorEmail(email);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            logger.debug("Usuário encontrado: {}", usuario.getEmail());
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                String token = jwtUtil.generateToken(email);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("userId", usuario.getId().toString());  // Adiciona o userId à resposta

                // Adiciona o log para verificar se os valores estão sendo corretamente definidos
                logger.debug("Gerando resposta com token: {} e userId: {}", token, usuario.getId());

                return ResponseEntity.ok(response);
            } else {
                logger.debug("Senha incorreta para o usuário: {}", email);
            }
        } else {
            logger.debug("Usuário não encontrado: {}", email);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        usuario.setCriadoEm(new Date());
        usuario.setAtualizadoEm(new Date());
        boolean isRegistered = usuarioService.cadastrarUsuario(usuario);
        if (!isRegistered) {
            logger.debug("Email já existe: {}", usuario.getEmail());
            return ResponseEntity.badRequest().body("Email já existe");
        }
        logger.debug("Usuário registrado com sucesso: {}", usuario.getEmail());
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Usuario> optionalUsuario = usuarioService.buscarPorEmail(email);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Buscando as skills associadas ao usuário
            List<UsuarioSkill> usuarioSkills = usuarioSkillService.getSkillsByUsuarioId(usuario.getId());

            // Transformando a lista de UsuarioSkill para incluir os detalhes das skills e níveis
            List<Map<String, Object>> detailedSkills = usuarioSkills.stream().map(skill -> {
                Map<String, Object> skillDetails = new HashMap<>();
                skillDetails.put("id", skill.getId());
                skillDetails.put("usuarioId", skill.getUsuarioId());
                skillDetails.put("skillId", skill.getSkillId());
                skillDetails.put("levelId", skill.getLevelId());

                // Usando os serviços injetados para obter os nomes
                String skillName = skillService.findNameById(skill.getSkillId());
                String levelName = levelService.findNameById(skill.getLevelId());

                skillDetails.put("skillName", skillName);
                skillDetails.put("levelName", levelName);

                return skillDetails;
            }).collect(Collectors.toList());

            // Criando um mapa para armazenar o usuário e suas skills detalhadas
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", usuario);
            response.put("skills", detailedSkills);

            return ResponseEntity.ok(response);
        }

        logger.debug("Usuário não encontrado com e-mail: {}", email);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public static class LoginRequest {
        private String email;
        private String senha;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}
