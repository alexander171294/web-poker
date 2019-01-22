import { LobbyComponent } from './components/lobby/lobby.component';
import { LoginComponent } from './components/login/login.component';
import { Routes } from '@angular/router';
import { RoomComponent } from './components/room/room.component';

export const ROUTES: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent},
  { path: 'lobby', component: LobbyComponent},
  { path: 'room', component: RoomComponent }
];
