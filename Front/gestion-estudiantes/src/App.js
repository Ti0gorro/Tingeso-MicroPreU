import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import StudentForm from './components/StudentForm';
import StudentList from './components/StudentList';
import StudentSummary from './components/StudentSummary';

function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <h1>Gestión de Estudiantes - TopEducation</h1>
        </header>
        <Routes>
          <Route path="/" element={<StudentList />} />
          <Route path="/nuevo-estudiante" element={<StudentForm />} />
          <Route path="/resumen/:rut" element={<StudentSummary />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
