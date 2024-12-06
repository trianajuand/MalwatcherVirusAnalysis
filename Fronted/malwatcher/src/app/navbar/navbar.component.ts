import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UsuarioService } from '../service/usuario.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  template: `<div class="navbar">
  <div class="logo">
    <img src="/assets/images/logo.png" alt="loguito" style="height: 60px; width: 60px;"/>
  </div>
  <div *ngIf="!isLoggedIn()" class="nav-buttons">
    <button class="btn home-btn">
      <i class="fa-solid fa-house" routerLink=""></i>
    </button>
    <button class="btn sign-in-btn" routerLink="inicioSesion"> Sign in </button>
    <button class="btn signup-btn" routerLink="registro">Sign up</button>
  </div>
  <div *ngIf="isLoggedIn()" class="nav-buttons">
    <button class="btn home-btn">
      <i class="fa-solid fa-house" routerLink=""></i>
    </button>
    <button class="btn sign-in-btn" routerLink="historial"> History </button>
    <button class="btn sign-in-btn" (click)="logout()"> LogOut </button>
  </div>
</div>
<br><br>
  `,
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor(private usuarioService: UsuarioService) { }

  isLoggedIn(): boolean {
    return this.usuarioService.isLoggedIn();
  }

  logout() {
    this.usuarioService.setLoggedIn(false);
  }
}

