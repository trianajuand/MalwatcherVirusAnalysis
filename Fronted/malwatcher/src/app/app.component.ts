import { RouterModule } from '@angular/router';
import { IndexComponent } from "./index/index.component";
import { NavbarComponent } from "./navbar/navbar.component";
import { FooterComponent } from "./footer/footer.component";
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, NavbarComponent, FooterComponent, RouterModule],
  template: '<app-navbar></app-navbar> <router-outlet></router-outlet> <app-footer></app-footer>',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title(title: any) {
    throw new Error('Method not implemented.');
  }
}