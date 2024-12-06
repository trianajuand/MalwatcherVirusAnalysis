import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Usuario } from '../usuario';
import { UsuarioService } from '../service/usuario.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-registro',
  standalone: true, 
  imports: [CommonModule, FormsModule],
  template: `
     <div class="login-container">
    <div class="login-left">
      <h2>Sign up and stop threats  </h2>
      <form>
        <div class="form-group">
          <label for="username">User name</label>
          <br/>
          <input id="username" type="text" [(ngModel)]="usuario.nombreUsuario" name="nombreUsuario" [ngModelOptions]="{standalone: true}" required  placeholder="MalWatcher123" />
        </div>
        <div class="form-group">
          <label for="correo"> Email</label>
          <br/>
          <input id="correo" type="text" [(ngModel)]="usuario.correo" name="correo" [ngModelOptions]="{standalone: true}" required placeholder="Malwatcher@malware.com" />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <br/>
          <input id="password" type="password" [(ngModel)]="usuario.contrasenia" name="password" [ngModelOptions]="{standalone: true}" required placeholder="MalWatcher0123#" />
        </div>
        <button type="button" (click)=" create() ">Register</button>

      </form>
    </div>

    <div class="popup1" *ngIf="popupVisible">
        <span class="close-button" (click)="closePopup()">&times;</span>
        <h2>Verification</h2>
        <p>"Dear user, we have sent a verification link to the email address provided. We ask that you check your inbox and spam folders, and click the link to confirm your account. This step is necessary to secure your profile and enable full access to our features. We appreciate your cooperation and await your confirmation to continue."</p>
        <div class="verification-code">
    <label for="verificationCode">Enter the verification code:</label>
    <input 
      type="text" 
      id="verificationCode" 
      maxlength="6" 
      [(ngModel)]="usuario.codigo" 
      placeholder="Código de 6 dígitos" 
      [pattern]="'\d{6}'" 
      required
    />
    <br/>
    <br/>

    <button (click)="verify()" [disabled]="!usuario.codigo || usuario.codigo.length !== 6">
    Verify Code
  </button>
  </div>
      </div>

    <div class="popup" *ngIf="incompleteFormPopupVisible">
      <span class="close-button" (click)="closeIncompleteFormPopup()">&times;</span>
       <h2>INCOMPLETE FORM</h2>
       <p>Please complete all fields before registering.</p>
     </div>

     <div class="popup" *ngIf="repitedEmailPopupVisible">
      <span class="close-button" (click)="closeRepitedEmailPopupVisible()">&times;</span>
       <h2>MAIL IN USE </h2>
       <p>Please enter another email that is not in use</p>
     </div>

     <div class="popup" *ngIf="successVerificationPopupVisible">
      <span class="close-button" (click)="closeSuccessVerificationPopup()">&times;</span>
      <h2>SUCCESSFUL VERIFICATION</h2>
      <p>Verification successful, account activated.</p>
    </div>

    <div class="popup" *ngIf="incorrectCodePopupVisible">
      <span class="close-button" (click)="closeIncorrectCodePopup()">&times;</span>
      <h2>INCORRECT CODE</h2>
      <p>Incorrect verification code.</p>
    </div>
  </div>`
  ,
  styleUrl: './registro.component.css'
})
export class RegistroComponent implements OnInit{

  usuarios: Usuario[] = [];
  incompleteFormPopupVisible = false;
  successVerificationPopupVisible = false;
  incorrectCodePopupVisible = false;
  repitedEmailPopupVisible = false;
  usuario: Usuario = new Usuario();
  constructor(private usuarioService:UsuarioService) {

  }
  
  ngOnInit(): void {
  
  }
  
  popupVisible = false;
  
  openPopup() {
    this.popupVisible = true;
  }
  
  closePopup() {
    this.popupVisible = false;
  }

  openIncompleteFormPopup() {
    this.incompleteFormPopupVisible = true;
  }
  
  closeIncompleteFormPopup() {
    this.incompleteFormPopupVisible = false;
  }

  openSuccessVerificationPopup() {
    this.successVerificationPopupVisible = true;
  }

  closeSuccessVerificationPopup() {
    this.successVerificationPopupVisible = false;
  }

  openIncorrectCodePopup() {
    this.incorrectCodePopupVisible = true;
  }

  closeIncorrectCodePopup() {
    this.incorrectCodePopupVisible = false;
  }

  openRepitedEmailPopupVisible() {
    this.repitedEmailPopupVisible = true;
  }

  closeRepitedEmailPopupVisible() {
    this.repitedEmailPopupVisible = false;
  }

  
  onSubmit(event: Event) {
    event.preventDefault(); 
    this.openPopup();
  }

  
 async create() {
    if (!this.usuario.nombreUsuario || !this.usuario.correo || !this.usuario.contrasenia) {
      this.openIncompleteFormPopup()
      return;
    } 
    
    try {
      const result = await this.usuarioService.createNewUsuario(this.usuario.nombreUsuario, this.usuario.correo, this.usuario.contrasenia);
      console.log(result); // Muestra el mensaje de éxito o error
      this.openPopup(); // Solo se abre el popup si la creación es exitosa
    } catch (error) {
      console.error('Error creating user:', error);
    }
  }
  
 
  async verify() {
    try {
      const result = await this.usuarioService.verifyUsuario(this.usuario.correo, this.usuario.codigo, this.usuario.nombreUsuario, this.usuario.contrasenia);
      console.log(result); // Muestra el mensaje de éxito o error
  
      if (result === "Cuenta verificada y usuario creado con éxito.") {
        this.openSuccessVerificationPopup(); 
        this.closePopup(); 
      } else {
        this.openIncorrectCodePopup(); 
        this.closePopup();
      }
    } catch (error) {
      this.openIncorrectCodePopup(); 
      this.closePopup();
    }
  }
  

  
  
  
}