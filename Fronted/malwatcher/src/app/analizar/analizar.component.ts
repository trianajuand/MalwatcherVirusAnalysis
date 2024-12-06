import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { VirustotalserviceService } from '../service/virustotalservice.service';

interface AntivirusResult {
  engineName: string;
  result: string;
  engineVersion: string;
}

interface AnalysisSummary {
  confirmedTimeout: number;
  failure: number;
  harmless: number;
  malicious: number;
  sha256: string;
  status: string;
  suspicious: number;
  timeout: number;
  typeUnsupported: number;
  undetected: number;
}

interface Caracteristicas {
  md5: string;
  sha1: string;
  ssdeep: string;
  tlsh: string;
  magika: string;
  size: string;
  magic: string;
  firstSubmissionDate: string;
  meaningfulName: string;
  typeTags: string[];
}

@Component({
  selector: 'app-analizar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="description">
      <div class="title">
        <h1>Análisis</h1>
        <br><br>
      </div>

      <!-- Mostrar mensaje de carga -->
      <div *ngIf="isLoading" class="loading">
        <p>Loading analysis results, please wait...</p>
      </div>

      <!-- Mostrar tabla de datos cuando isLoading sea false -->
      <div *ngIf="!isLoading">
        <div class="toolbar">
          <button class="toolbar-button" (click)="showArchivo()">Archive</button>
          <button class="toolbar-button" (click)="showEspecificaciones()">Specs</button>
          <button class="toolbar-button" (click)="showCaracteristicas()">Characteristics</button>
        </div>

        <!-- Div para Archivo -->
        <div *ngIf="mostrarArchivo && !mostrarCaracteristicas" class="detalles">
          <div class="tabla">
            <table >
              <thead>
                <tr>
                  <td >Anti Virus Name</td>
                  <td >File Status</td>
                  <td >Engine Version</td>
                  <td >Not Detected</td>
                  <td >Malicious</td>
                  <td >Suspicious</td>
                  <td >Harmless</td>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let result of antivirusResults">
                  <td>{{ result.engineName }}</td>
                  <td>{{ result.result }}</td>
                  <td>{{ result.engineVersion }}</td>
                  <td>{{ summary.undetected }}</td>
                  <td>{{ summary.malicious }}</td>
                  <td>{{ summary.suspicious }}</td>
                  <td>{{ summary.harmless }}</td>
                </tr>
              </tbody>
            </table>
            <br><br>
          </div>
        </div>

        <!-- Div para Especificaciones -->
        <div *ngIf="!mostrarArchivo && !mostrarCaracteristicas" class="detalles">
          <br><br>
          <div class="toolbar">
            <h2>Basic properties</h2>
          </div>
          <div class="tabla">
            <table>
              <tbody>
                <tr>
                  <td>SHA-256</td>
                  <td>{{ summary.sha256 }}</td>
                </tr>
                <tr>
                  <td>Status</td>
                  <td>{{ summary.status }}</td>
                </tr>
                <tr>
                  <td>Timeout</td>
                  <td>{{ summary.timeout }}</td>
                </tr>
                <tr>
                  <td>Type Unsupported</td>
                  <td>{{ summary.typeUnsupported }}</td>
                </tr>
                <tr>
                  <td>Confirmed Timeout</td>
                  <td>{{ summary.confirmedTimeout }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <br><br>
        </div>

        <!-- Div para Características -->
        <div *ngIf="mostrarCaracteristicas" class="caracteristicas">
          <br><br>
          <div class="toolbar">
            <h2>File Features</h2>
          </div>

  <div class="tabla">
    <table>
      <tbody>
        <tr><td>MD5</td><td>{{ caracteristicas.md5 }}</td></tr>
        <tr><td>SHA1</td><td>{{ caracteristicas.sha1 }}</td></tr>
        <tr><td>SSDEEP</td><td>{{ caracteristicas.ssdeep }}</td></tr>
        <tr><td>TLSH</td><td>{{ caracteristicas.tlsh }}</td></tr>
        <tr><td>Magika</td><td>{{ caracteristicas.magika }}</td></tr>
        <tr><td>Size</td><td>{{ caracteristicas.size }}</td></tr>
        <tr><td>Magic</td><td>{{ caracteristicas.magic }}</td></tr>
        <tr><td>First Submission Date</td><td>{{ caracteristicas.firstSubmissionDate }}</td></tr>
        <tr><td>Meaningful Name</td><td>{{ caracteristicas.meaningfulName }}</td></tr>
        <tr><td>Type Tags</td><td>{{ caracteristicas.typeTags ? caracteristicas.typeTags.join(', ') : '' }}</td></tr>
      </tbody>
    </table>
  </div>
</div>

    <br><br>
  `,
  styleUrls: ['./analizar.component.css']
})
export class AnalizarComponent implements OnInit {
  mostrarArchivo = true;
  mostrarCaracteristicas = false;
  isLoading = true;

  antivirusResults: AntivirusResult[] = [];
  summary: AnalysisSummary = {
    confirmedTimeout: 0,
    failure: 0,
    harmless: 0,
    malicious: 0,
    sha256: '',
    status: '',
    suspicious: 0,
    timeout: 0,
    typeUnsupported: 0,
    undetected: 0
  };
  caracteristicas: Caracteristicas = {
    md5: '',
    sha1: '',
    ssdeep: '',
    tlsh: '',
    magika: '',
    size: '',
    magic: '',
    firstSubmissionDate: '',
    meaningfulName: '',
    typeTags: [] // Array vacío por defecto
  };
  

  constructor(
    private virustotalservice: VirustotalserviceService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const analysisIndex = +this.route.snapshot.paramMap.get('id')!;
    const data = this.virustotalservice.getData(analysisIndex);
    if (data) {
      this.cargarDatosDeAnalisis(data);
      this.isLoading = false;
    } else {
      console.error("No se encontraron datos de análisis para el índice proporcionado.");
      this.isLoading = false;
    }
  }

  cargarDatosDeAnalisis(data: any) {
    this.antivirusResults = data.antivirusResults.map((result: any) => ({
      engineName: result.engineName,
      result: result.result,
      engineVersion: result.engineVersion
    }));

    this.summary = {
      confirmedTimeout: data.confirmedTimeout,
      failure: data.failure,
      harmless: data.harmless,
      malicious: data.malicious,
      sha256: data.sha256,
      status: data.status,
      suspicious: data.suspicious,
      timeout: data.timeout,
      typeUnsupported: data.typeUnsupported,
      undetected: data.undetected
    };

    this.caracteristicas = data.caracteristicas;
  }

  showArchivo() {
    this.mostrarArchivo = true;
    this.mostrarCaracteristicas = false;
  }

  showEspecificaciones() {
    this.mostrarArchivo = false;
    this.mostrarCaracteristicas = false;
  }

  async showCaracteristicas() {
    this.isLoading = true;
    this.mostrarArchivo = false;
    this.mostrarCaracteristicas = true;
  
    try {
      const scanId = this.summary.sha256; // Usamos el SHA-256 como ID
      if (scanId && scanId.trim()) { // Verificamos que `scanId` no sea nulo o vacío
        this.caracteristicas = await this.virustotalservice.getReport(scanId);
      } else {
        console.error("SHA-256 no disponible para obtener el reporte.");
        this.isLoading = false;
        return; // Salimos de la función si `scanId` es nulo o vacío
      }
    } catch (error) {
      console.error("Error al cargar las características:", error);
    } finally {
      this.isLoading = false;
    }
  }
  
}
