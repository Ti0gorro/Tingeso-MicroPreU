import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

const StudentSummary = () => {
  const [summary, setSummary] = useState(null);
  const { rut } = useParams();

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const response = await axios.get(`http://localhost:8082/api/examenes/reporte/${rut}`);
        setSummary(response.data);
      } catch (error) {
        console.error('Error al obtener el resumen del estudiante:', error);
      }
    };

    fetchSummary();
  }, [rut]);

  return (
    <div>
      {summary ? (
        <div>
          <h1>Resumen de {summary.nombreEstudiante}</h1>
          <p><strong>RUT:</strong> {summary.rutEstudiante}</p>
          <p><strong>Número de Exámenes Rendidos:</strong> {summary.numeroExamenesRendidos}</p>
          <p><strong>Promedio Puntaje Exámenes:</strong> {summary.promedioPuntajeExamenes}</p>
          <p><strong>Monto Total Arancel:</strong> ${summary.montoTotalArancel}</p>
          <p><strong>Tipo de Pago:</strong> {summary.tipoPago}</p>
          <p><strong>Total Cuotas Pactadas:</strong> {summary.totalCuotasPactadas}</p>
          <p><strong>Cuotas Pagadas:</strong> {summary.cuotasPagadas}</p>
          <p><strong>Monto Total Pagado:</strong> ${summary.MontoTotalPagado}</p>
          <p><strong>Fecha del Último Pago:</strong> {summary.fechaUltimoPago}</p>
          <p><strong>Saldo Por Pagar:</strong> ${summary.saldoPorPagar}</p>
          <p><strong>Cuotas con Retraso:</strong> {summary.cuotasConRetraso}</p>
        </div>
      ) : (
        <p>Cargando resumen...</p>
      )}
      <Link to="/" style={{ textDecoration: 'none', color: 'blue' }}>Volver al Menú Principal</Link>
    </div>
  );
};

export default StudentSummary;
