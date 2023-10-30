import React, { useState } from 'react';
import axios from 'axios';

const StudentForm = ({ setShowForm }) => {
  const currentYear = new Date().getFullYear();
  const [studentData, setStudentData] = useState({
    RUT: '',
    apellidos: '',
    nombres: '',
    fechaNacimiento: '',
    tipoColegio: '',
    nombreColegio: '',
    añoEgreso: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "RUT" && value.length <= 9) {
      let formattedValue = value.replace(/[^0-9]/g, '');
      if (formattedValue.length === 8) {
        formattedValue = `${formattedValue.slice(0, 8)}-${formattedValue.slice(7)}`;
      }
      setStudentData(prevData => ({ ...prevData, [name]: formattedValue }));
    } else {
      setStudentData(prevData => ({ ...prevData, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formattedData = {
        rut: studentData.RUT,
        apellidos: studentData.apellidos.toUpperCase(),
        nombres: studentData.nombres.toUpperCase(),
        fechaNacimiento: studentData.fechaNacimiento,
        tipoColegioProcedencia: parseInt(studentData.tipoColegio),
        nombreColegio: studentData.nombreColegio.toUpperCase(),
        anoEgresoColegio: parseInt(studentData.añoEgreso)
      };

      const response = await axios.post('http://localhost:8080/api/estudiantes', formattedData);

      if (response.status === 200) {
        alert('Estudiante registrado con éxito');
        setShowForm(false);
      }
    } catch (error) {
      alert('Error al registrar el estudiante');
    }
  };

  return (
    <div style={containerStyle}>
      <h2 style={titleStyle}>Registro de Estudiantes</h2>
      <form onSubmit={handleSubmit} style={formStyle}>
        <div style={labelStyle}>
          <span>RUT:</span>
          <input type="text" style={inputStyle} name="RUT" value={studentData.RUT} onChange={handleChange} required />
        </div>
        <div style={labelStyle}>
          <span>Apellidos:</span>
          <input type="text" style={inputStyle} name="apellidos" value={studentData.apellidos} onChange={handleChange} required />
        </div>
        <div style={labelStyle}>
          <span>Nombres:</span>
          <input type="text" style={inputStyle} name="nombres" value={studentData.nombres} onChange={handleChange} required />
        </div>
        <div style={labelStyle}>
          <span>Fecha de Nacimiento:</span>
          <input type="date" style={inputStyle} name="fechaNacimiento" min="1990-01-01" max="2011-12-31" value={studentData.fechaNacimiento} onChange={handleChange} required />
        </div>
        <div style={labelStyle}>
          <span>Tipo de Colegio:</span>
          <select style={inputStyle} name="tipoColegio" value={studentData.tipoColegio} onChange={handleChange} required>
            <option value="1">Municipal</option>
            <option value="2">Subvencionado</option>
            <option value="3">Privado</option>
          </select>
        </div>
        <div style={labelStyle}>
          <span>Nombre del Colegio:</span>
          <input type="text" style={inputStyle} name="nombreColegio" value={studentData.nombreColegio} onChange={handleChange} required />
        </div>
        <div style={labelStyle}>
          <span>Año de Egreso:</span>
          <input type="number" style={inputStyle} name="añoEgreso" min="2018" max={currentYear} value={studentData.añoEgreso} onChange={handleChange} required />
        </div>
        <div style={buttonContainerStyle}>
          <button type="submit" style={btnStyle}>Registrar</button>
          <button type="button" onClick={() => setShowForm(false)} style={btnStyle}>Volver</button>
        </div>
      </form>
    </div>
  );
};

const containerStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  padding: '30px',
  backgroundColor: '#f5f5f5',
  borderRadius: '10px',
  boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)',
  maxWidth: '600px',
  margin: 'auto'
};

const titleStyle = {
  marginBottom: '30px',
  color: '#333',
  borderBottom: '2px solid #007BFF',
  paddingBottom: '10px'
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '20px',
  width: '100%'
};

const labelStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  gap: '20px',
  width: '100%',
  fontSize: '16px',
  color: '#555'
};

const inputStyle = {
  flex: 1,
  padding: '10px',
  borderRadius: '5px',
  border: '1px solid #ccc',
  fontSize: '16px'
};

const buttonContainerStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  marginTop: '30px'
};

const btnStyle = {
  padding: '10px 20px',
  backgroundColor: '#007BFF',
  border: 'none',
  borderRadius: '5px',
  color: 'white',
  cursor: 'pointer',
  fontSize: '16px',
  transition: 'background-color 0.3s',
  '&:hover': {
    backgroundColor: '#0056b3'
  }
};

export default StudentForm;