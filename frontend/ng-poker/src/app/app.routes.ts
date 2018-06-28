import { LoginComponent } from './components/login/login.component';
import { Routes } from '@angular/router';

export const ROUTES: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent}
];
