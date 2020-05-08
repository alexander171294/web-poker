import { IndividualChipStatus } from '../../../../services/network/epprProtocol/game/ChipStatus';
import { RoomService } from 'src/app/services/network/room.service';
import { Component, OnInit, ViewChildren, QueryList, OnDestroy, HostListener } from '@angular/core';
import { PlayerSnapshot } from './PlayerSnapshot';
import { Card } from '../../cards/dual-stack/Card';
import { RxEType } from 'src/app/services/network/ReactionEvents';
import { VcardComponent } from '../../vcard/vcard.component';
import { ChipsService } from 'src/app/services/memory/chips.service';
import { Pots } from 'src/app/services/network/epprProtocol/game/Pots';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserMenuControllerService } from '../../user-menu-actions/user-menu-controller.service';

@Component({
  selector: 'app-table-poker',
  templateUrl: './poker.component.html',
  styleUrls: ['./poker.component.scss']
})
export class PokerComponent implements OnInit, OnDestroy {

  private static MAX_PLAYERS = 10;

  public players: PlayerSnapshot[];
  public zones = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'];

  public tableCards: Card[];
  public availablePositions: boolean[] = [];
  public pot: number;
  public splittedPots: number[];
  public dealed: boolean;
  public dealerPosition: number = -1;
  public myPosition: number;
  public resultMode: boolean;

  public rxESubscription: Subscription;

  public info: string;

  @ViewChildren(VcardComponent) vcards: QueryList<VcardComponent>;

  constructor(private room: RoomService,
              private chips: ChipsService,
              private route: ActivatedRoute,
              private umc: UserMenuControllerService) { }

  ngOnInit() {
    this.players = [];
    this.tableCards = [];
    for (let i = 0; i < PokerComponent.MAX_PLAYERS; i++) {
      this.players.push(new PlayerSnapshot());
      this.availablePositions.push(false);
    }
    // SNAPSHOT HISTORY:
    const round = this.route.params['value'].round;
    if (round) {
      let firstSnap = true;
      this.room.reactionEvent.subscribe(evt => {
        if (evt.type === RxEType.SNAPSHOT) {
          if (firstSnap) {
            firstSnap = false;
            this.room.getSnapshot(round);
          } else {
            this.processSnapshot(evt);
          }
        }
      });
      return;
    }
    //
    this.rxESubscription = this.room.reactionEvent.subscribe(evt => {
      if (evt.type === RxEType.ANNOUNCEMENT) {
        // this.announcement =
        // console.log('Original', this.players[evt.data.position]);
        if (this.players[evt.data.position] == null || this.players[evt.data.position].playerDetails.name == null) {
          const nPlayer = new PlayerSnapshot();
          nPlayer.playerDetails.chips = evt.data.chips;
          nPlayer.playerDetails.image = evt.data.avatar;
          nPlayer.playerDetails.name = evt.data.user;
          nPlayer.playerDetails.userID = evt.data.userID;
          nPlayer.inGame = false;
          this.players[evt.data.position] = nPlayer;
        }
        this.availablePositions[evt.data.position] = false;
        // si llega primero el ingress:
        if (this.myPosition && evt.data.position === this.myPosition) {
          this.chips.set(evt.data.chips);
        }
      }
      if (evt.type === RxEType.START_IN) {
        if (evt.data > 0) {
          this.info = 'Game start in ' + evt.data + (evt.data !== 1 ? ' seconds' : ' second');
        }
        if (evt.data === -1) {
          this.info = 'Game in pause';
        }
        // reseting final statuses:
        this.clearInitialVariables();
      }
      if (evt.type === RxEType.ROUND_START) {
        this.info = undefined; // removing info box
        this.dealerPosition = evt.data.dealerPosition;
        // reseting final statuses:
        this.clearInitialVariables();
        this.tableCards = [null, null, null, null, null];
      }
      if (evt.type === RxEType.BLINDS) {
        this.pot = evt.data.sbChips + evt.data.bbChips;
        this.players[evt.data.sbPosition].playerDetails.chips -= evt.data.sbChips;
        this.players[evt.data.bbPosition].playerDetails.chips -= evt.data.bbChips;
        this.players[evt.data.sbPosition].actualBet = evt.data.sbChips;
        this.players[evt.data.bbPosition].actualBet = evt.data.bbChips;
        this.chips.setBigBlind(evt.data.bbChips);
        this.chips.setSmallBlind(evt.data.sbChips);
        if (evt.data.bbPosition === this.myPosition) {
          this.chips.substract(evt.data.bbChips);
        }
        if (evt.data.sbPosition === this.myPosition) {
          this.chips.substract(evt.data.sbChips);
        }
        this.dealed = true;
      }
      if (evt.type === RxEType.CARD_DIST) {
        this.players[evt.data.position].inGame = true;
        if (evt.data.position !== this.myPosition) {
          this.players[evt.data.position].upsidedown = true;
          this.players[evt.data.position].cards = evt.data.cards;
        }
      }
      if (evt.type === RxEType.ME_CARD_DIST) {
        this.players[evt.data.position].upsidedown = false;
        this.players[evt.data.position].cards = evt.data.cards;
      }
      if (evt.type === RxEType.WAITING_FOR) {
        // data.position+' for: '+data.remainingTime
        this.players.forEach((player, idx) => {
          if (player) {
            this.players[idx].timeRest = idx === evt.data.position ? evt.data.remainingTime : undefined;
            if (idx === evt.data.position) {
              this.vcards.toArray()[idx].startTimeRest(evt.data.remainingTime);
            } else {
              this.vcards.toArray()[idx].finishActions();
            }
          }
        });
      }
      if (evt.type === RxEType.DONE_ACTION) {
        this.players[this.myPosition].timeRest = undefined;
      }
      if (evt.type === RxEType.INGRESS) {
        this.myPosition = evt.data.position;
        for (let i = 0; i < PokerComponent.MAX_PLAYERS; i++) {
          this.availablePositions[i] = false;
        }
        // si llega primero el Announcement:
        if (this.players[this.myPosition]) {
          this.chips.set(this.players[this.myPosition].playerDetails.chips);
        }
      }
      if (evt.type === RxEType.FLOP) {
        this.players.forEach((player, idx) => {
          this.vcards.toArray()[idx].finishActions();
        });
        this.tableCards[0] = evt.data[0];
        this.tableCards[1] = evt.data[1];
        this.tableCards[2] = evt.data[2];
        // clear chips
        this.clearTableChips();
      }
      if (evt.type === RxEType.TURN) {
        this.players.forEach((player, idx) => {
          this.vcards.toArray()[idx].finishActions();
        });
        this.tableCards[3] = evt.data;
        // clear chips
        this.clearTableChips();
      }
      if (evt.type === RxEType.RIVER) {
        this.players.forEach((player, idx) => {
          this.vcards.toArray()[idx].finishActions();
        });
        this.tableCards[4] = evt.data;
        // clear chips
        this.clearTableChips();
      }
      if (evt.type === RxEType.SNAPSHOT) {
        this.processSnapshot(evt);
      }
      if (evt.type === RxEType.DEFINE_POSITION) {
        evt.data.forEach(freePositions => {
          this.availablePositions[freePositions] = true;
        });
      }
      if (evt.type === RxEType.DECISION_INFORM) {
        this.players[evt.data.position].playerDetails.chips -= evt.data.ammount;
        this.players[evt.data.position].actualBet += evt.data.ammount;
        if (evt.data.position === this.myPosition) {
          this.chips.substract(evt.data.ammount);
        }
        this.pot += evt.data.ammount;
      }
      if (evt.type === RxEType.SHOW_OFF) {
        this.players.forEach((player, idx) => {
          if (player) {
            this.players[idx].timeRest = undefined;
          }
        });
        evt.data.positionCards.forEach((cards, idx) => {
          if (cards) {
            this.players[idx].cards = [cards.first, cards.second];
            this.players[idx].upsidedown = false;
          }
        });
        this.clearTableChips();
      }
      if (evt.type === RxEType.POTS) {
        this.splittedPots = evt.data.pots;
      }
      if (evt.type === RxEType.FOLD) {
        if (evt.data.position !== this.myPosition) {
          this.players[evt.data.position].cards = undefined;
        }
        this.players[evt.data.position].timeRest = undefined;
        this.players[evt.data.position].inGame = true;
      }
      if (evt.type === RxEType.RESULT_SET) {
        // result set
        console.info('RESULT SET', evt.data);
        this.players.forEach(player => {
          if (player) {
            player.winner = false;
            player.inGame = false;
          }
        });
        evt.data.winners.forEach(winner => {
          this.resultMode = true;
          this.players[winner.position].winner = true;
          this.players[winner.position].playerDetails.chips += winner.pot;
          if (winner.position === this.myPosition) {
            this.chips.add(winner.pot);
          }
        });
      }
      if (evt.type === RxEType.DEPOSIT_SUCCESS) {
        if (this.players[this.myPosition]) {
          this.players[this.myPosition].playerDetails.chips += evt.data.chips;
          this.chips.add(evt.data.chips);
        }
      }
      if (evt.type === RxEType.DEPOSIT_ANNOUNCEMENT) {
        // si no somos nostors porque ya nos contamos
        if (evt.data.position !== this.myPosition) {
          this.players[evt.data.position].playerDetails.chips += evt.data.quantity;
        }
      }
      if (evt.type === RxEType.CHIP_STATUS) {
        evt.data.status.forEach((ics: IndividualChipStatus) => {
          this.players[ics.position].playerDetails.chips = ics.chips;
        });
      }
      if (evt.type === RxEType.LEAVE) {
        this.players[evt.data.position] = new PlayerSnapshot();
        if (evt.data.position === this.dealerPosition) {
          this.dealerPosition = undefined;
        }
        if (evt.data.position === this.myPosition) {
          window.close();
        }
        // this.availablePositions[evt.data.position] = true;
      }
    });
  }

  trySeat(position: number, evt, userID: number) {
    if (this.availablePositions[position]) {
      if (this.players[position].playerDetails.name) {
        // TODO: improve this alert:
        alert('This position is in use.');
      } else {
        console.warn('Sitting...');
        this.room.selectPosition(position);
      }
    } else {
      if (userID) {
        this.umc.show(evt.clientX, evt.clientY, userID);
      }
    }
  }

  private clearTableChips() {
    this.players.forEach(player => {
      if (player) {
        player.actualBet = 0;
      }
    });
  }

  private processSnapshot(evt) {
    // console.log('SNAPSHOT', evt.data);
    this.dealerPosition = evt.data.dealerPosition;
    this.chips.setBigBlind(evt.data.bigBlind);
    this.chips.setSmallBlind(evt.data.smallBlind);
    if (evt.data.pots) {
      this.splittedPots = evt.data.pots;
    }
    if (evt.data.myPosition >= 0) {
      this.myPosition = evt.data.myPosition;
    }
    // PRE-FLOP:
    if (evt.data.roundStep >= 1) {
      this.dealed = true;
      this.tableCards[0] = null;
      this.tableCards[1] = null;
      this.tableCards[2] = null;
      this.tableCards[3] = null;
      this.tableCards[4] = null;
    }
    evt.data.players.forEach((player, idx) => {
      if (player != null) {
        const nPlayer = new PlayerSnapshot();
        nPlayer.playerDetails.chips = player.chips;
        if (this.myPosition === idx) {
          // console.log('Settings chips by snapshot:');
          this.chips.set(player.chips);
        }
        nPlayer.playerDetails.image = player.photo;
        nPlayer.playerDetails.name = player.nick;
        nPlayer.playerDetails.userID = player.userID;
        nPlayer.actualBet = player.actualBet;
        nPlayer.inGame = player.inGame;
        if (this.dealed) {
          console.log('CARDS', player.haveCards);
          if (player.haveCards) {
            nPlayer.cards = player.cards ? player.cards : [true, true];
          }
          nPlayer.upsidedown = evt.data.roundStep <= 5 && !player.showingCards;
        }
        this.players[idx] = nPlayer;
      }
    });
    // FLOP:
    if (evt.data.roundStep >= 2) {
      this.tableCards[0] = evt.data.communityCards[0];
      this.tableCards[1] = evt.data.communityCards[1];
      this.tableCards[2] = evt.data.communityCards[2];
      this.tableCards[3] = null;
      this.tableCards[4] = null;
    }
    // TURN:
    if (evt.data.roundStep >= 3) {
      this.tableCards[3] = evt.data.communityCards[3];
      this.tableCards[4] = null;
    }
    // River:
    if (evt.data.roundStep >= 4) {
      this.tableCards[4] = evt.data.communityCards[4];
    }
    if (evt.data.waitingFor) {
      this.players[evt.data.waitingFor].timeRest = 30; // FIXME: change this for rest of time from backend.
      // this.vcards.toArray()[evt.data.waitingFor].startTimeRest(30);
    }
  }

  public clearInitialVariables() {
    this.dealed = false;
    this.players.forEach(player => {
      player.cards = [];
      player.upsidedown = false;
      player.actualBet = 0;
    });
    this.tableCards = [];
    this.resultMode = false;
    this.pot = 0;
    this.splittedPots = undefined;
  }

  ngOnDestroy(): void {
    this.rxESubscription.unsubscribe();
  }

  // @HostListener('window:unload', ['$event'])
  // unloadNotification($event: any) {
  //   this.room.leave();
  // }

  // @HostListener('window:beforeunload', ['$event'])
  // beforeunloadNotification($event: any) {
  //   return false;
  // }


}

