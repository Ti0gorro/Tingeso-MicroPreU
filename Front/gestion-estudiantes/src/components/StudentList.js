import React, { useState, useEffect } from 'react';
import axios from 'axios';
import CuotasManager from './CuotasManager'; 

const StudentList = ({ setShowForm }) => {
  const [students, setStudents] = useState([]);

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/estudiantes');
        setStudents(response.data);
      } catch (error) {
        console.error('Error al obtener la lista de estudiantes', error);
      }
    };

    fetchStudents();
  }, []);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h2>Listado de Estudiantes</h2>
      <button onClick={() => setShowForm(true)} style={btnStyle}>Nuevo Estudiante</button>
      {students.length === 0 ? (
        <p>No hay estudiantes registrados.</p>
      ) : (
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>RUT</th>
              <th style={thStyle}>Nombre Estudiante</th>
              <th style={thStyle}>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {students.map((student) => (
              <tr key={student.rut} style={trStyle}>
                <td style={tdStyle}>{student.rut}</td>
                <td style={tdStyle}>{`${student.nombres} ${student.apellidos}`}</td>
                <td style={tdStyle}>
                  <CuotasManager rutEstudiante={student.rut} tipoColegio={Number(student.tipoColegioProcedencia)} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

const btnStyle = {
  padding: '10px 15px',
  backgroundColor: '#007BFF',
  border: 'none',
  borderRadius: '4px',
  color: 'white',
  cursor: 'pointer',
  marginBottom: '20px',
  fontSize: '16px',
};

const tableStyle = {
  borderCollapse: 'collapse',
  width: '80%',
  border: '1px solid black',
};

const thStyle = {
  border: '1px solid black',
  padding: '8px',
  backgroundColor: '#f2f2f2'
};

const trStyle = {
  borderBottom: '1px solid black'
};

const tdStyle = {
  border: '1px solid black',
  padding: '8px',
  textAlign: 'center'
};

export default StudentList;
