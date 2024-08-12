import React, { useState, useEffect } from "react";
import styles from "./AddSkillModal.module.css";
import { getAvailableSkills, getLevels } from "../../Services/Api";

const AddSkillModal = ({ onClose, onSave }) => {
  const [selectedSkill, setSelectedSkill] = useState('');
  const [selectedLevel, setSelectedLevel] = useState('');
  const [skills, setSkills] = useState([]);
  const [levels, setLevels] = useState([]);

  useEffect(() => {
    const fetchSkillsAndLevels = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const skillsData = await getAvailableSkills(token);
          setSkills(skillsData);
          const levelsData = await getLevels(token);
          setLevels(levelsData);
        } catch (error) {
          console.error('Error fetching skills or levels:', error);
        }
      }
    };

    fetchSkillsAndLevels();
  }, []);

  const handleSave = () => {
    const usuarioId = localStorage.getItem('usuarioId'); // Alterado de userId para usuarioId
    console.log('Retrieved usuario ID:', usuarioId); // Adicionar log para verificar o ID do usuário
    if (!usuarioId) {
      console.error('Usuario ID not found');
      return;
    }

    if (!selectedSkill || !selectedLevel) {
      console.error('Skill or level not selected');
      return;
    }

    onSave(selectedSkill, selectedLevel); // Passar skillId e levelId
  };

  return (
    <div className={styles.modal}>
      <div className={styles.modalContent}>
        <span>Select a skill to add:</span>
        <select
          value={selectedSkill}
          onChange={(e) => setSelectedSkill(e.target.value)}
        >
          <option value="" disabled>Select Skill</option>
          {skills.map(skill => (
            <option key={skill.id} value={skill.id}>{skill.name}</option>
          ))}
        </select>

        <span>Select a level:</span>
        <select
          value={selectedLevel}
          onChange={(e) => setSelectedLevel(e.target.value)}
        >
          <option value="" disabled>Select Level</option>
          {levels.map(level => (
            <option key={level.id} value={level.id}>{level.nome}</option>
          ))}
        </select>

        <button onClick={handleSave}>Save</button>
        <button onClick={onClose}>Close</button>
      </div>
    </div>
  );
};

export default AddSkillModal;
