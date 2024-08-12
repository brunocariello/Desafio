package br.com.loginneki.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.loginneki.entity.Usuario;
import br.com.loginneki.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return false;
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setCriadoEm(new java.util.Date());
        usuario.setAtualizadoEm(new java.util.Date());
        usuarioRepository.save(usuario);
        return true;
    }

    public Usuario autenticarUsuario(String email, String senha) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
            );
            return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email " + email));
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação: " + e.getMessage());
        }
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID " + id));

        if (usuarioRepository.findByEmail(usuarioAtualizado.getEmail()).isPresent() &&
            !usuarioAtualizado.getEmail().equals(usuarioExistente.getEmail())) {
            throw new RuntimeException("Email já existe");
        }

        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        usuarioExistente.setAtualizadoEm(new java.util.Date());
        return usuarioRepository.save(usuarioExistente);
    }

    public boolean deletarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Usuario obterInformacoesDoUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal(); // Obtém o email do usuário logado
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email " + email));
    }
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Long getUsuarioIdByEmail(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        return usuarioOpt.map(Usuario::getId).orElse(null);
    }

}
