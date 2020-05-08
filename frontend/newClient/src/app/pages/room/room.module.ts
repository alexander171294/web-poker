import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VcardComponent } from './vcard/vcard.component';
import { PokerComponent } from './tables/poker/poker.component';
import { ChatBoxComponent } from './chat-box/chat-box.component';
import { ActionBoxComponent } from './action-box/action-box.component';
import { MazoComponent } from './cards/mazo/mazo.component';
import { DualStackComponent } from './cards/dual-stack/dual-stack.component';
import { CardComponent } from './cards/card/card.component';
import { ChipsComponent } from './chips/chips/chips.component';
import { StackerComponent } from './chips/stacker/stacker.component';
import { RouterModule, Routes } from '@angular/router';
import { RoomComponent } from './room.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { DepositComponent } from './deposit/deposit.component';
import { ConnectingComponent } from './connecting/connecting.component';
import { FormsModule } from '@angular/forms';
import { BetSelectorComponent } from './action-box/bet-selector/bet-selector.component';
import { UserMenuActionsComponent } from './user-menu-actions/user-menu-actions.component';

const routes: Routes = [
  { path: '',  component: RoomComponent },
];

@NgModule({
  declarations: [
    RoomComponent,
    VcardComponent,
    PokerComponent,
    ChatBoxComponent,
    ActionBoxComponent,
    MazoComponent,
    DualStackComponent,
    CardComponent,
    ChipsComponent,
    StackerComponent,
    NavBarComponent,
    DepositComponent,
    ConnectingComponent,
    BetSelectorComponent,
    UserMenuActionsComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule
  ]
})
export class RoomModule { }
