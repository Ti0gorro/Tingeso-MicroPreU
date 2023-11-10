import React, { useState, useEffect } from 'react';
import axios from 'axios';
import queryString from 'query-string';

const CuotasManager = ({ rutEstudiante, tipoColegio }) => {
  const obtenerNumeroMaxCuotas = (tipoColegio) => {
    switch (tipoColegio) {
      case 1: return 10;
      case 2: return 7;
      case 3: return 4;
      default: return 0;
    }
  };

  const [cuotas, setCuotas] = useState([]);
  const [generar, setGenerar] = useState(false);
  const [numeroCuotas, setNumeroCuotas] = useState(1);

  const numeroMaxCuotas = obtenerNumeroMaxCuotas(tipoColegio);

  useEffect(() => {
    setNumeroCuotas(Math.max(1, numeroMaxCuotas));
  }, [numeroMaxCuotas]);

  useEffect(() => {
    const fetchCuotas = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/api/cuotas/${rutEstudiante}`);
        setCuotas(response.data);
      } catch (error) {
        console.error('Error al obtener cuotas:', error);
      }
    };

    fetchCuotas();
  }, [rutEstudiante]);

  const handleGenerarCuotas = async () => {
    try {
      const data = queryString.stringify({
        rutEstudiante,
        numeroCuotas
      });

      const response = await axios.post(`http://localhost:8081/api/cuotas/generar`, data, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      });

      setCuotas(response.data);
      setGenerar(false);
    } catch (error) {
      console.error('Error al generar cuotas:', error);
    }
  };

  const handlePagarCuota = async (idCuota) => {
    try {
      const response = await axios.post(`http://localhost:8081/api/cuotas/pago/${idCuota}`);
      setCuotas(cuotas.map(cuota => cuota.id === idCuota ? response.data : cuota));
    } catch (error) {
      console.error('Error al registrar el pago:', error);
    }
  };

  return (
    <div>
      <h3>Cuotas del Estudiante {rutEstudiante}</h3>
      {cuotas.length > 0 ? (
        <ul>
          {cuotas.map(cuota => (
            <li key={cuota.id}>
              {`Cuota ${cuota.id}: $${cuota.monto} - ${cuota.estado} - Vence: ${cuota.fechaDeVencimiento}`}
              {cuota.estado === "PENDIENTE" && (
                <button onClick={() => handlePagarCuota(cuota.id)}>Pagar</button>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <p>No hay cuotas registradas.</p>
      )}

      {generar && (
        <div>
          <label>
            Número de Cuotas:
            <input
              type="number"
              value={numeroCuotas}
              onChange={(e) => setNumeroCuotas(Math.min(e.target.valueAsNumber, obtenerNumeroMaxCuotas(tipoColegio)))}
              min="1"
              max={numeroMaxCuotas}
            />
          </label>
          <button onClick={handleGenerarCuotas}>Generar Cuotas</button>
        </div>
      )}

      {!generar && cuotas.length === 0 && (
        <button onClick={() => setGenerar(true)}>Generar Cuotas</button>
      )}
    </div>
  );
};

export default CuotasManager;
