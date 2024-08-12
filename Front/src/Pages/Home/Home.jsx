import React, { useState, useEffect } from "react";
import SkillList from "../../Components/SkillList/SkillList";
import AddSkillModal from "../../Components/AddSkillModal/AddSkillModal";
import Header from "../../Components/Header/Header";
import styles from "./Home.module.css";
import backgroundImage from "../../assets/fundoalternativo.jpg";

import { getSkills, addSkill, updateSkill, deleteSkill, getUserInfo } from "../../Services/Api";

const Home = () => {
  const [skills, setSkills] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [userEmail, setUserEmail] = useState('');
  const [usuarioId, setUsuarioId] = useState(null);
  const [userSkills, setUserSkills] = useState([]);

  useEffect(() => {
    const fetchSkillsAndUserInfo = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const skillsData = await getSkills(token);
          setSkills(skillsData);

          const userInfo = await getUserInfo(token);
          setUserEmail(userInfo.usuario.email);
          setUsuarioId(userInfo.usuario.id);
          setUserSkills(userInfo.skills); // Define as skills a partir da resposta do endpoint

          localStorage.setItem('usuarioId', userInfo.usuario.id);
        } catch (error) {
          console.error('Error fetching skills or user info', error);
        }
      } else {
        window.location.href = '/login';
      }
    };

    fetchSkillsAndUserInfo();
  }, []);

  const handleAddSkill = async (skillId, levelId) => {
    const token = localStorage.getItem('token');
    const usuarioId = localStorage.getItem('usuarioId');

    if (token && usuarioId) {
      try {
        await addSkill(token, { usuarioId, skillId, levelId });
        const updatedSkills = await getSkills(token);
        setSkills(updatedSkills);
        setShowModal(false);
      } catch (error) {
        console.error('Error adding skill', error);
      }
    } else {
      console.error('Token or usuarioId is missing');
    }
  };

  const handleUpdateSkill = async (skillId, levelId) => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        await updateSkill(token, { skillId, levelId });
        const updatedSkills = await getSkills(token);
        setSkills(updatedSkills);
      } catch (error) {
        console.error('Error updating skill', error);
      }
    }
  };

  const handleDeleteSkill = async (skillId) => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        await deleteSkill(token, skillId);
        const updatedSkills = await getSkills(token);
        setSkills(updatedSkills);
        setUserSkills(updatedSkills); // Atualiza as skills do usuário também
      } catch (error) {
        console.error('Error deleting skill', error);
      }
    }
  };

  return (
    <div className={styles.container}>
      <Header 
        onLogout={() => { 
          localStorage.removeItem('token'); 
          localStorage.removeItem('usuarioId');
          window.location.href = '/login'; 
        }} 
        userEmail={userEmail}
        onAddSkill={() => setShowModal(true)}  
        userSkills={userSkills}  
        onUpdateSkill={handleUpdateSkill}
        onDeleteSkill={handleDeleteSkill} 
      />
      <div className={styles.content} style={{ backgroundImage: `url(${backgroundImage})` }}>
        <SkillList skills={skills} />
      </div>
      {showModal && <AddSkillModal onClose={() => setShowModal(false)} onSave={handleAddSkill} />}
    </div>
  );
};

export default Home;
