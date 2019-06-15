import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { LobbyComponent } from './lobby/lobby.component';
import { ProfileSectionComponent } from './lobby/profile-section/profile-section.component';
import { BodyComponent } from './lobby/body/body.component';
import { RoomComponent } from './room/room/room.component';
import { HeaderComponent } from './utils/box/header/header.component';
import { FooterComponent } from './utils/box/footer/footer.component';
import { PopupComponent } from './utils/box/popup/popup.component';
import { PokerComponent } from './room/tables/poker/poker.component';
import { VcardComponent } from './room/vcard/vcard.component';
import { ChatBoxComponent } from './room/chat-box/chat-box.component';
import { ActionBoxComponent } from './room/action-box/action-box.component';
import { ChipsComponent } from './utils/chips/chips/chips.component';
import { StackerComponent } from './utils/chips/stacker/stacker.component';
import { CardComponent } from './utils/cards/card/card.component';
import { MazoComponent } from './utils/cards/mazo/mazo.component';
import { DualStackComponent } from './utils/cards/dual-stack/dual-stack.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LobbyComponent,
    ProfileSectionComponent,
    BodyComponent,
    RoomComponent,
    HeaderComponent,
    FooterComponent,
    PopupComponent,
    PokerComponent,
    VcardComponent,
    ChatBoxComponent,
    ActionBoxComponent,
    ChipsComponent,
    StackerComponent,
    CardComponent,
    MazoComponent,
    DualStackComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
