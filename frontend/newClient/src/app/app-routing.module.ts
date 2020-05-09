import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { LobbyComponent } from './pages/lobby/lobby.component';


const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'login/:err', component: LoginComponent},
  {path: 'lobby', component: LobbyComponent},
  {path: 'room/:id', loadChildren: () => import('./pages/room/room.module').then(mod => mod.RoomModule) },
  {path: 'room/:id/:round', loadChildren: () => import('./pages/room/room.module').then(mod => mod.RoomModule) },
  { path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
