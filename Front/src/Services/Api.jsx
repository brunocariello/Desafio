import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const login = async (email, senha) => {
  try {
    const response = await api.post('/api/login', { email, senha });
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('userId', response.data.userId);
    return response.data;
  } catch (error) {
    console.error('Erro ao fazer login:', error);
    throw error;
  }
};

export const signup = async (email, senha) => {
  try {
    const response = await api.post('/register', { email, senha });
    return response.data;
  } catch (error) {
    console.error('Erro ao cadastrar usuário:', error);
    throw error;
  }
};

export const getSkills = async (token) => {
  try {
    const response = await api.get('/skills', {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Erro ao obter skills:', error);
    throw error;
  }
};

export const getLevels = async (token) => {
  try {
    const response = await api.get('/levels', {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Erro ao obter níveis:', error);
    throw error;
  }
};

export const addSkill = async (token, { usuarioId, skillId, levelId }) => {
  try {
    const response = await fetch('http://localhost:8080/usuario-skill', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ usuarioId, skillId, levelId })
    });

    if (!response.ok) {
      throw new Error(`Failed to add skill, status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Erro ao adicionar skill:', error);
    throw error;
  }
};

export const updateSkill = async (token, { skillId, levelId }) => {
  try {
    const response = await api.put(
      `/skills/${skillId}`,
      { levelId },
      { headers: { Authorization: `Bearer ${token}` } },
    );
    return response.data;
  } catch (error) {
    console.error('Erro ao atualizar skill:', error);
    throw error;
  }
};

export const deleteSkill = async (token, skillId) => {
  try {
    const response = await api.delete(`/usuario-skill/${skillId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Erro ao excluir skill:', error);
    throw error;
  }
};

export const getUserInfo = async (token) => {
  try {
    const response = await api.get('/api/me', { 
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Erro ao obter informações do usuário:', error);
    throw error;
  }
};

export const getAvailableSkills = async (token) => {
  try {
    const response = await api.get('/skills/available', {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Erro ao obter skills disponíveis:', error);
    throw error;
  }
};

export default api;
