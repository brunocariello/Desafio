// src/Routes/Routes.jsx
import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Login from '../Pages/Login/Login';
import Cadastro from '../Pages/Cadastro/Cadastro';
import Home from '../Pages/Home/Home';

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  );
};

export default AppRoutes;
