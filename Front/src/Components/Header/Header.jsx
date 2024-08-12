import React from "react";
import styles from "./Header.module.css";

const Header = ({ onLogout, userEmail, onAddSkill, userSkills, onUpdateSkill, onDeleteSkill }) => {
  return (
    <div className={styles.header}>
      <span className={styles.userEmail}>{userEmail}</span>
      <div className={styles.userSkills}>
        {userSkills && userSkills.length > 0 ? (
          <table className={styles.skillsTable}>
            <thead>
              <tr>
                <th>Skill</th>
                <th>Level</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {userSkills.map((skill) => (
                <tr key={skill.id}>
                  <td>{skill.skillName}</td>
                  <td>{skill.levelName}</td>
                  <td>
                    <button
                      className={styles.editButton}
                      onClick={() => onUpdateSkill(skill.skillId, skill.levelId)}
                    >
                      Editar
                    </button>
                    <button
                      className={styles.deleteButton}
                      onClick={() => onDeleteSkill(skill.skillId)} // Corrigido: Chamando onDeleteSkill corretamente
                    >
                      Excluir
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <div className={styles.noSkills}>No skills available</div>
        )}
      </div>
      <button className={styles.addSkillButton} onClick={onAddSkill}>
        Adicionar Skill
      </button>
      <button className={styles.logoutButton} onClick={onLogout}>
        Logout
      </button>
    </div>
  );
};

export default Header;
