import { Component } from '@angular/core';
import { UsuarioService } from '../service/usuario.service'; 
import { CommonModule } from '@angular/common';
import { Usuario } from '../usuario';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inicio-sesion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template:`
<div class="login-container">
  <div class="login-left">
    <h2> 👍 Sign in and stop the threats 👍</h2>
    <form (ngSubmit)="login()">
      <div class="form-group">
        <label for="nombreUsuario">User Name</label>
        <br/>
        <input id="nombreUsuario" type="text" [(ngModel)]="usuario.nombreUsuario" name="nombreUsuario" [ngModelOptions]="{standalone: true}" required placeholder="Malwatcher1234" />
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <br/>
        <input id="password" type="password" [(ngModel)]="usuario.contrasenia" name="password" [ngModelOptions]="{standalone: true}" placeholder="MalWatcher0123#" />
      </div>
      <button type="button" (click)="login()">Login</button>
    </form>
    <div *ngIf="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>

  <div class="popup" *ngIf="NotGoodPopupPopupVisible">
      <span class="close-button" (click)="closeNotGoodPopupPopupVisible()">&times;</span>
       <h2>WAIT!! I THINK SOMETHING IS WRONG</h2>
       <br/><br/>
       <p> The Username or the Password are incorrect, please Try Again.</p>
     </div>

</div>
`

  ,
  styleUrls: ['./inicio-sesion.component.css']
})

export class InicioSesionComponent {
  
  usuario: Usuario = new Usuario();
  errorMessage: string | null = null;
  NotGoodPopupPopupVisible = false;

  constructor(private UsuarioService: UsuarioService, private router: Router) { }

  openNotGoodPopupPopupVisible() {
    this.NotGoodPopupPopupVisible = true;
  }
  
  closeNotGoodPopupPopupVisible() {
    this.NotGoodPopupPopupVisible = false;
  }
  
  async login() {
    this.errorMessage = null;  
    try {
      const usuario: Usuario = await this.UsuarioService.checkLogIn(this.usuario.nombreUsuario, this.usuario.contrasenia);
      if(!usuario.nombreUsuario){
        this.openNotGoodPopupPopupVisible
      } else{
        this.UsuarioService.setLoggedIn(true);
        this.router.navigate(['/']); 
        this.errorMessage  = 'Correct credentials'
      }
      } catch (error: any) {
        console.error('Error during login:', error);
        this.openNotGoodPopupPopupVisible();
    }
  }
}