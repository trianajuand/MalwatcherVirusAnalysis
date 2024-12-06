import { Routes } from '@angular/router';
import { IndexComponent } from './index/index.component';
import { RegistroComponent } from './registro/registro.component';
import { Component } from '@angular/core';
import { InicioSesionComponent } from './inicio-sesion/inicio-sesion.component';
import { HistorialComponent } from './historial/historial.component';
import { AnalizarComponent } from './analizar/analizar.component';
Component;

export const routeConfig: Routes = [


  { path: 'analizar/:id', component: AnalizarComponent },

  { 
    path: '', 
    component: IndexComponent, 
    title: 'index'
   }, 
   {
    path: 'registro',
    component: RegistroComponent,
    title:'registro'
   },
   {
    path: 'inicioSesion',
    component: InicioSesionComponent,
    title:'inicioSesion'
   },
   {
    path: 'historial',
    component: HistorialComponent,
    title: 'historial'
   },
   {
    path: 'analizar',
    component: AnalizarComponent,
    title: 'analizar'
   }
   



];

export default routeConfig; 