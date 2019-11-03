import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { LobbyComponent } from './pages/lobby/lobby.component';
import { RoomComponent } from './pages/room/room.component';


const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'lobby', component: LobbyComponent},
  {path: 'room', component: RoomComponent},
  { path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
