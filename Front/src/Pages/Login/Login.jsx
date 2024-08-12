import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { login as loginService } from "../../Services/Api";
import { useAuth } from "../../Context/AuthContext"; // Importa o hook useAuth
import backgroundImage from "../../assets/fundoalternativo.jpg"; // Ajuste o caminho conforme necessário
import styles from "./Login.module.css"; // Importa o módulo CSS

const Login = () => {
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [gravarSenha, setGravarSenha] = useState(false);
  const navigate = useNavigate();
  const { login: setAuthToken } = useAuth(); // Utiliza o hook useAuth

  useEffect(() => {
    const savedLogin = localStorage.getItem("login");
    const savedSenha = localStorage.getItem("senha");

    if (savedLogin && savedSenha) {
      setLogin(savedLogin);
      setSenha(savedSenha);
      setGravarSenha(true);
    }
  }, []);

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await loginService(login, senha);
      const { token } = response;

      if (token) {
        setAuthToken(token); // Atualiza o token no contexto

        if (gravarSenha) {
          localStorage.setItem("login", login);
          localStorage.setItem("senha", senha);
        } else {
          localStorage.removeItem("login");
          localStorage.removeItem("senha");
        }

        localStorage.setItem("token", token);
        navigate("/home");
      } else {
        alert("Credenciais inválidas. Tente novamente!");
      }
    } catch (error) {
      alert("Ocorreu um erro ao tentar logar. Por favor, tente novamente.");
    }
  };

  return (
    <div
      className={styles.loginPage}
      style={{ backgroundImage: `url(${backgroundImage})` }}
    >
      <form onSubmit={handleLogin}>
        <div className={styles.formGroup}>
          <h1>Login</h1>
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
              type={mostrarSenha ? "text" : "password"}
              id="senha"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
          </div>

          <div className={styles.formGroup}>
            <button
              type="button"
              className={styles.btnVisualizar}
              onClick={() => setMostrarSenha(!mostrarSenha)}
            >
              {mostrarSenha ? "Esconder" : "Mostrar"}
            </button>
          </div>
        </div>

        <div className={styles.formGroup}>
          <input
            type="checkbox"
            id="gravarSenha"
            checked={gravarSenha}
            onChange={() => setGravarSenha(!gravarSenha)}
          />
          <label htmlFor="gravarSenha">Gravar Senha</label>
        </div>

        <div className={styles.formGroup}>
          <button type="submit" className={styles.btnEntrar}>
            Entrar
          </button>
        </div>

        <div className={styles.formGroup}>
          <button
            type="button"
            className={styles.btnCadastrar}
            onClick={() => navigate("/cadastro")}
          >
            Cadastrar-se
          </button>
        </div>
      </form>
    </div>
  );
};

export default Login;
