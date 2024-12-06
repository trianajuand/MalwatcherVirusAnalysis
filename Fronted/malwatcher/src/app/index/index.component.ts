import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { VirustotalserviceService } from '../service/virustotalservice.service';
import { UsuarioService } from '../service/usuario.service';

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="description">
      <div class="title">
        <div class="logoPortada">
          <img src="/assets/images/logo.png" alt="loguito" style="height:150px; width: 150px; margin-right: 100px;"/>
        </div>
        <h1>Mal-Watcher</h1>
      </div>
      <br><br>
      <p class="textDescription">
      Through our site, users can upload malware files and, after uploading, a detailed analysis is displayed. This includes malware characteristics, its severity level, and a description of its behavior or impact. The goal is to offer a tool similar to a malware analysis platform.      </p>
      <div class="divider"></div>

      <div class="subirArchivo">
        <i class="fa-solid fa-file-arrow-up" style="font-size: 100px;"></i>
        <br>
        <input type="file" id="fileInput" style="display: none;" (change)="onFileChange($event)" />
        <button class="btn btn-primary" (click)="selectFile()">Upload File</button>

        <div *ngIf="uploadedFile">
          <br>
          <p>Uploaded File: {{ uploadedFile.name }}</p>
        </div>
        <br>

        <button class="btn btn-primary" (click)="subirArchivo()" [disabled]="isUploading">Analyze File</button>

        <div *ngIf="isUploading" class="spinner">
          <p>Analyzing File, Please wait for a moment...</p>
          <div class="loading-icon"></div>
        </div>
      </div>

      <div *ngIf="showSuccessMessage" class="popup-message success-message">
        <div class="message-content">
          <h3>¡File Analyzed correctly!</h3>
          <p>The file analysis was completed. You can see the report in the analysis section.</p>
          <button (click)="closeSuccessMessage()">Cerrar</button>
        </div>
      </div>

      <div *ngIf="showErrorMessage" class="popup-message error-message">
        <div class="message-content">
          <h3>Error uploading file</h3>
          <p>There was an error uploading the file or the file is larger than 200 MB.</p>
          <button (click)="closeErrorMessage()">Cerrar</button>
        </div>
      </div>

      <div class="popup" *ngIf="popupVisible">
        <span class="close-button" (click)="closePopup()">&times;</span>
        <h2>STOP THERE!!</h2>
        <p>Before you can upload your files, you need to have an account so you can save all your interactions in history. Please log in first before continuing.</p>
      </div>

      <div class="popup" *ngIf="missingFilePopupVisible">
        <span class="close-button" (click)="closeMissingFilePopup()">&times;</span>
        <h2>MISSING FILE</h2>
        <p>Please select a file before attempting to analyze it.</p>
      </div>
    </div>
  `,
  styleUrls: ['./index.component.css']
})
export class IndexComponent {
  uploadedFile: File | null = null;
  isUploading = false;
  popupVisible = false;
  missingFilePopupVisible = false;
  showSuccessMessage = false;
  showErrorMessage = false;

  constructor(
    private virusTotalService: VirustotalserviceService,
    private router: Router,
    private usuarioService: UsuarioService
  ) {}

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.uploadedFile = input.files[0];
      console.log('Archivo seleccionado:', this.uploadedFile);
    }
  }

  openPopup() {
    this.popupVisible = true;
  }

  closePopup() {
    this.popupVisible = false;
  }

  openMissingFilePopup() {
    this.missingFilePopupVisible = true;
  }

  closeMissingFilePopup() {
    this.missingFilePopupVisible = false;
  }

  selectFile() {
    if (!this.isLoggedIn()) {
      this.openPopup();
      return;
    }
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  async subirArchivo() {
    if (!this.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }
  
    if (!this.uploadedFile) {
      this.openMissingFilePopup();
      return;
    }
  
    this.isUploading = true;
    const userId = this.usuarioService.getUsuarioLogeado().id; // Asegúrate de obtener el ID del usuario autenticado; este es solo un ejemplo
  
    try {
      const result = await this.virusTotalService.scanFile(this.uploadedFile, userId); // Pasa `userId` como segundo argumento
  
      if (!this.isLoggedIn()) {
        this.router.navigate(['/']);
        return;
      }
  
      const analysisIndex = this.virusTotalService.getAllData().length - 1;
  
      this.showSuccessMessage = true;
      setTimeout(() => {
        this.showSuccessMessage = false;
        this.router.navigate(['/analizar', analysisIndex]);
      }, 3000);
    } catch (error) {
      console.error('Error al subir el archivo:', error);
      this.showErrorMessage = true;
    } finally {
      this.isUploading = false;
    }
  }

  closeSuccessMessage() {
    this.showSuccessMessage = false;
  }

  closeErrorMessage() {
    this.showErrorMessage = false;
  }

  isLoggedIn(): boolean {
    return this.usuarioService.isLoggedIn();
  }
}
