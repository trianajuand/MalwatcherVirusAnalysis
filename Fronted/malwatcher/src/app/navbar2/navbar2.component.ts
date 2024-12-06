import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar2',
  standalone: true,
  imports: [RouterModule],
  template: `
    <div class="navbar">
      <div class="logo">
        <img src="/assets/images/logo.png" alt="loguito" style="height: 60px; width: 60px;"/>
      </div>
      <div class="nav-buttons">
        <button class="btn home-btn">
          <i class="fa-solid fa-house" routerLink=""></i>
        </button>
        <button class="btn sign-in-btn" routerLink="historial"> Historial </button>
        <button class="btn sign-in-btn"> LogOut </button>
      </div>
    </div>
    <br><br>
  `,
  styleUrls: ['./navbar2.component.css'] 
})
export class Navbar2Component {}
