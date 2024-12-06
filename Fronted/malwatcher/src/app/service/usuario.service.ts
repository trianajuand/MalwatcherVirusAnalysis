import { Injectable } from '@angular/core';
import { Usuario } from '../usuario';



@Injectable({
  providedIn: 'root',
})
export class UsuarioService {

   baseURL = "http://localhost:8082/usuario";

  constructor() { }

  private loggedIn = false; 

  

  async getHistorial(userId: number): Promise<string[]> {
    const response = await fetch(`http://localhost:8082/usuario/${userId}/historial`);


    if (!response.ok) {
      throw new Error('Error al obtener el historial');
    }
    return await response.json();
  }

  // Método para añadir un archivo al historial
  async addFileToHistorial(userId: number, sha256: string): Promise<void> {
    const response = await fetch(`${this.baseURL}/${userId}/historial`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(sha256)
    });
    if (!response.ok) {
      throw new Error('Error al añadir al historial');
    }
  }
  
  async createNewWithJSON(newUsuario: Usuario): Promise<string> {
    try {
      const response = await fetch(this.baseURL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUsuario)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error('Error creating user:', error);
      throw error;
    }
  }

  async createNewUsuario(nombreUsuario: string, correo: string, contrasenia: string): Promise<string> {
    try {
      const params = new URLSearchParams({
        nombreUsuario,
        correo,
        contrasenia
      });

      const response = await fetch(`${this.baseURL}/create?${params.toString()}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await response.text(); // La respuesta es un mensaje de éxito o error
      return result;
    } catch (error) {
      console.error('Error creating user:', error);
      throw error;
    }
  }

  async verifyUsuario(correo: string, codigoVerificacion: string, nombreUsuario: string, contrasenia: string): Promise<string> {
    try {
      const params = new URLSearchParams({
        correo,
        codigoVerificacion,
        nombreUsuario,
        contrasenia
      });

      const response = await fetch(`${this.baseURL}/verify?${params.toString()}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await response.text(); // La respuesta es un mensaje de éxito o error
      return result;
    } catch (error) {
      console.error('Error verifying user:', error);
      throw error;
    }
  }

  async checkLogIn(nombreUsuario: string, contrasenia: string): Promise<Usuario> {
    const params = new URLSearchParams({
      nombreUsuario,
      contrasenia
    })

    const url = `${this.baseURL}/checklogin?${params.toString()}`
    const response = await fetch(url , {
     method: 'POST',
     headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    const json = await response.json();
    sessionStorage.setItem('usuario' , JSON.stringify(json))
    return response.ok? json : Promise.reject(await response.text())
    
  }

  getUsuarioLogeado(): Usuario {
    const usuario = sessionStorage.getItem('usuario');
    if(usuario){
      const usuarioLogged : Usuario = JSON.parse(usuario);
      return usuarioLogged;
    } 
    return new Usuario();
  }


  async getAllUsuarios(): Promise<Usuario[]> {
    try {
      const response = await fetch(this.baseURL, {
        method: 'GET',
      });

      if (response.status === 204) {
        console.log("No hay usuarios disponibles");
        return []; // Devuelve una lista vacía si no hay contenido
      } else if (response.status === 202) {
        return await response.json(); // Devuelve la lista de usuarios
      } else {
        throw new Error(`Unexpected response status: ${response.status}`);
      }
    } catch (error) {
      console.error('Error fetching users:', error);
      throw error;
    }
  }

  async countAllUsuarios(): Promise<number> {
    try {
      const response = await fetch(this.baseURL, {
        method: 'GET',
      });

      if (response.status === 204) {
        console.log("No hay usuarios en la base de datos.");
        return 0; // Devuelve 0 si no hay usuarios
      } else if (response.status === 202) {
        const count = await response.json();
        return count as number;
      } else {
        throw new Error(`Unexpected response status: ${response.status}`);
      }
    } catch (error) {
      console.error('Error fetching user count:', error);
      throw error;
    }
  }

  async userExists(id: number): Promise<boolean> {
    try {
      const response = await fetch(`${this.baseURL}/${id}`, {
        method: 'GET',
      });

      if (response.status === 202) {
        return true; // Usuario existe
      } else if (response.status === 204) {
        return false; // Usuario no existe
      } else {
        throw new Error(`Unexpected response status: ${response.status}`);
      }
    } catch (error) {
      console.error('Error checking user existence:', error);
      throw error;
    }
  }

  async getUserById(id: number): Promise<Usuario | null> {
    try {
      const response = await fetch(`${this.baseURL}/${id}`, {
        method: 'GET',
      });

      if (response.status === 202) {
        const user = await response.json();
        return user as Usuario; // Devuelve el usuario si se encontró
      } else if (response.status === 404) {
        console.log("Usuario no encontrado");
        return null; // Devuelve null si no se encontró
      } else {
        throw new Error(`Unexpected response status: ${response.status}`);
      }
    } catch (error) {
      console.error('Error fetching user by ID:', error);
      throw error;
    }
  }

  async updateUser(id: number, newUsuario: Usuario): Promise<string> {
    try {
      const response = await fetch(`${this.baseURL}?id=${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUsuario)
      });

      if (response.status === 202) {
        return "Usuario updated successfully";
      } else if (response.status === 226) {
        return "New Usuarioname already taken";
      } else if (response.status === 404) {
        return "Usuario not found";
      } else {
        return "Error on update";
      }
    } catch (error) {
      console.error('Error updating user:', error);
      throw error;
    }
  }

  setLoggedIn(status: boolean): void {
    this.loggedIn = status;
  }

  // Obtener el estado de inicio de sesión
  isLoggedIn(): boolean {
    return this.loggedIn;
  }

  async obtenerHistorial(userId: number): Promise<string[]> {
    const url = `${this.baseURL}/${userId}/historial`;
    const response = await fetch(url, { method: 'GET' });

    if (!response.ok) {
      throw new Error(`Error al obtener historial: ${response.statusText}`);
    }

    return response.json();
  }

  // Método para agregar un archivo al historial del usuario
  async agregarArchivoAlHistorial(userId: number, sha256: string): Promise<void> {
    const url = `${this.baseURL}/${userId}/historial`;
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(sha256),
    });

    if (!response.ok) {
      throw new Error(`Error al agregar al historial: ${response.statusText}`);
    }
  }



}
