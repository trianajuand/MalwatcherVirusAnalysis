import { Injectable } from '@angular/core';
import { UsuarioService } from '../service/usuario.service';

@Injectable({
  providedIn: 'root'
})
export class VirustotalserviceService {
  baseURL = "http://localhost:8082/api/virustotal";
  private analysisData: any[] = []; // Arreglo para almacenar múltiples análisis
  private historial: { sha256: string, meaningfulName: string }[] = []; // Arreglo para el historial

  constructor(private usuarioService: UsuarioService) {}

  // Método para analizar el archivo y obtener el resultado
  async scanFile(file: File, userId: number): Promise<any> {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch(`${this.baseURL}/scan`, {
      method: 'POST',
      body: formData
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const result = await response.json(); // Aquí recibes el resultado con el `sha256`
    this.addData(result); // Almacenar los datos en el arreglo

    // Extrae el `sha256` del resultado y guárdalo en el historial
    const sha256 = result.sha256; // Asegúrate de que `sha256` esté presente en `result`
    if (sha256) {
      try {
        await this.usuarioService.agregarArchivoAlHistorial(userId, sha256); // Usar el `userId` pasado como parámetro
        console.log('Archivo añadido al historial');
      } catch (error) {
        console.error('Error al agregar el archivo al historial:', error);
      }
    } else {
      console.warn('No se encontró sha256 en el resultado del escaneo');
    }

    return result;
  }

  // Método para almacenar el resultado en el arreglo
  addData(data: any) {
    console.log("Guardando nuevo análisis en el servicio:", data);
    this.analysisData.push(data);
  }

  // Método para obtener un análisis específico
  getData(index: number) {
    console.log("Obteniendo análisis en el índice:", index);
    return this.analysisData[index];
  }

  // Método para obtener todos los análisis
  getAllData() {
    return this.analysisData;
  }

  // Método para obtener el reporte del análisis por su ID
  async getReport(scanId: string): Promise<any> {
    const response = await fetch(`${this.baseURL}/report/${scanId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`Error al obtener el reporte: ${response.status}`);
    }

    const reportData = await response.json();
    return reportData;
  }


 



  
  
}