import { MaterialModule } from './material.module';
import { LoginComponent } from './components/login/login.component';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { LoginBoxComponent } from './components/login/login-box/login-box.component';
import { SignupBoxComponent } from './components/login/signup-box/signup-box.component';
import { StompService } from './utils/stomp.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginBoxComponent,
    SignupBoxComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule
  ],
  providers: [StompService],
  bootstrap: [AppComponent]
})
export class AppModule { }