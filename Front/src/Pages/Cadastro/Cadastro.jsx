// src/Pages/Cadastro.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Atualize o import para useNavigate
import { signup } from '../../Services/Api'; // Importação da função de cadastro
import styles from './Cadastro.module.css'; // Importa o módulo CSS
import backgroundImage from '../../assets/fundoalternativo.jpg'; // Ajuste o caminho conforme necessário

const Cadastro = () => {
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const navigate = useNavigate(); // Use navigate em vez de history

  const handleCadastro = async (e) => {
    e.preventDefault();

    if (senha !== confirmarSenha) {
      alert('As senhas não coincidem!');
      return;
    }

    try {
      const response = await signup(login, senha);
      if (response) {
        alert('Cadastro realizado com sucesso!');
        navigate('/'); // Redirecionar para a página de login usando navigate
      } else {
        alert('Erro no cadastro. Tente novamente!');
      }
    } catch (error) {
      alert('Ocorreu um erro ao tentar cadastrar. Por favor, tente novamente.');
    }
  };

  return (
    <div className={styles.signupPage} style={{ backgroundImage: `url(${backgroundImage})` }}>
      <form onSubmit={handleCadastro}>
        <div className={styles.formGroup}>
          <h1>Cadastrar-se</h1>
          <label htmlFor="login">Email:</label>
          <input
            type="text"
            id="login"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            required
          />
          <label htmlFor="senha">Senha:</label>
          <div className={styles.inputContainer}>
            <input
              type={mostrarSenha ? 'text' : 'password'}
              id="senha"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
            <button
              type="button"
              className={styles.btnVisualizar}
              onClick={() => setMostrarSenha(!mostrarSenha)}
            >
              {mostrarSenha ? 'Esconder' : 'Mostrar'}
            </button>
          </div>
        </div>

        <div className={styles.formGroup}>
          <label htmlFor="confirmarSenha">Confirmar Senha:</label>
          <input
            type={mostrarSenha ? 'text' : 'password'}
            id="confirmarSenha"
            value={confirmarSenha}
            onChange={(e) => setConfirmarSenha(e.target.value)}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <button type="submit" className={styles.btnSalvar}>
            Salvar
          </button>
        </div>
      </form>
    </div>
  );
};

export default Cadastro;
