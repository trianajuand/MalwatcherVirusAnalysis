import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../service/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-historial',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="description">
      <div class="title">
        <h1>History</h1>
        <br>
        <br>
      </div>  

      <div class="tabla">
        <table>
          <thead>
            <tr>
              <th style="color: black;">SHA256</th>
              <th style="color: black;">File Status</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let item of historial">
              <td>{{ item.sha256 }}</td>
              <td>{{ item.estado || 'Desconocido' }}</td> <!-- Ajusta si tienes un estado específico -->
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <br>
    <br>
  `,
  styleUrls: ['./historial.component.css']
})
export class HistorialComponent implements OnInit {
  historial: { sha256: string; estado?: string }[] = []; // Definición del tipo de datos

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    if (!this.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }
    console.log(this.usuarioService.getUsuarioLogeado())
    try {
      const shaList = await this.usuarioService.obtenerHistorial(this.usuarioService.getUsuarioLogeado().id); // Asegúrate de obtener el ID correcto
      this.historial = shaList.map(sha256 => ({ sha256, estado: 'Analizado' })); // Puedes ajustar el estado según corresponda
    } catch (error) {
      console.error('Error al obtener el historial:', error);
    }
  }

  isLoggedIn(): boolean {
    return this.usuarioService.isLoggedIn();
  }

}
