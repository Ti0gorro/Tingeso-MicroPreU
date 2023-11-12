import React, { useState } from 'react';

const CsvUploader = ({ onUploadResult }) => {
  const [file, setFile] = useState(null);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!file) {
      alert('Por favor selecciona un archivo');
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch('http://localhost:8082/api/examenes/importar', {
        method: 'POST',
        body: formData
      });

      if (response.ok) {
        const result = await response.text();
        console.log(result);
        if (onUploadResult) {
          onUploadResult(true, 'Archivo subido con éxito');
        }
      } else {
        throw new Error('Error en la subida del archivo');
      }
    } catch (error) {
      console.error('Error al subir el archivo:', error);
      alert('Error al subir el archivo: ' + error.message);
      if (onUploadResult) {
        onUploadResult(false, 'Error al subir el archivo: ' + error.message);
      }
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input type="file" accept=".csv" onChange={handleFileChange} />
      <button type="submit">Subir Archivo</button>
    </form>
  );
};

export default CsvUploader;
