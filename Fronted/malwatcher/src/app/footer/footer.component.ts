import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  template: `
   <div class="equipo">
    <h2>Our Team</h2>
    <br>
  <br>
    <div class="equipo-contenido">
      <div class="equipo-columna">
        <h3>Databases</h3>
      <br>
        <p>Gabriel Alejandro Ruiz Cruz</p>
      </div>
      <div class="equipo-columna">
        <h3>Front-end</h3>
      <br>
        <p>Dylan Mathyus Hospital Herrera</p>
      <br>
        <p>Germán Octavio Vela Cardozo</p>
      </div>
      <div class="equipo-columna">
        <h3>Back-end</h3>
      <br>
        <p>Juan Diego Triana Mejía</p>
  `,
  styleUrl: './footer.component.css'
})
export class FooterComponent {

}
