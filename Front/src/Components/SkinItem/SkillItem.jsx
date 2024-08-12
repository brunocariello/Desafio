import React from "react";
import styles from "./SkillItem.module.css";

// Importe as imagens diretamente
import javaImage from "../../assets/java.jpg";
import reactImage from "../../assets/react.jpg";
import phytonImage from "../../assets/python.jpg";

const SkillItem = ({ skill }) => {
  // Mapeie o nome da skill para a imagem correspondente
  const getImageUrl = () => {
    switch (skill.name.toLowerCase()) {
      case 'java':
        return javaImage;
      case 'react':
        return reactImage;
      case 'phyton':
        return phytonImage;
      default:
        return null; // Ou uma imagem padrão se você tiver uma
    }
  };

  const imageUrl = getImageUrl();

  return (
    <div className={styles.skillItem}>
      <img src={imageUrl} alt={skill.name} className={styles.skillImage} />
      <div className={styles.skillContent}>
        <h3 className={styles.skillName}>{skill.name}</h3>
        <p className={styles.skillDescription}>{skill.description}</p>
      </div>
    </div>
  );
};

export default SkillItem;
