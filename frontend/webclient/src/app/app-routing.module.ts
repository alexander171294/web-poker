import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LobbyComponent } from './lobby/lobby.component';
import { RoomComponent } from './room/room/room.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    data: { title: 'WebPoker' }
  },
  {
    path: 'lobby',
    component: LobbyComponent,
    data: { title: 'WebPoker' }
  },
  { path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'room',
    component: RoomComponent,
    data: { title: 'Room' }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
