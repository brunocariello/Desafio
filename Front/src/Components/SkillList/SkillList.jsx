import React from "react";
import SkillItem from "../SkinItem/SkillItem";
import styles from "./SkillList.module.css";

const SkillList = ({ skills }) => {
  return (
    <div className={styles.skillList}>
      {skills.map((skill) => (
        <SkillItem key={skill.id} skill={skill} />
      ))}
    </div>
  );
};

export default SkillList;
