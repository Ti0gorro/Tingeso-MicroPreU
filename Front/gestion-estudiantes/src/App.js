import React, { useState } from 'react';
import './App.css';
import StudentForm from './components/StudentForm';
import StudentList from './components/StudentList';

function App() {
  const [showForm, setShowForm] = useState(false);

  return (
    <div className="App">
      <header className="App-header">
        <h1>Gestión de Estudiantes - TopEducation</h1>
      </header>
      {showForm ? <StudentForm setShowForm={setShowForm} /> : <StudentList setShowForm={setShowForm} />}
    </div>
  );
}

export default App;
