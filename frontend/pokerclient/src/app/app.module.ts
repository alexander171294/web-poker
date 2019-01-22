import { LobbyComponent } from './components/lobby/lobby.component';
import { UserService } from './services/user.service';
import { MaterialModule } from './modules/material.module';
import { LoginComponent } from './components/login/login.component';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { LoginBoxComponent } from './components/login/login-box/login-box.component';
import { SignupBoxComponent } from './components/login/signup-box/signup-box.component';
import { StompService } from './utils/stomp.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LobbyServer } from './services/lobby.server';
import { RoomComponent } from './components/room/room.component';
import { TableComponent } from './components/room/table/table.component';
import { ChatComponent } from './components/room/chat/chat.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginBoxComponent,
    SignupBoxComponent,
    LobbyComponent,
    RoomComponent,
    TableComponent,
    ChatComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [StompService, UserService, LobbyServer],
  bootstrap: [AppComponent]
})
export class AppModule { }
