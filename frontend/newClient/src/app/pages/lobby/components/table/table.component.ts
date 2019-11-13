import { Component, OnInit, Input } from '@angular/core';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

  @Input() rooms: RoomResponse[];

  constructor(private router: Router) { }

  ngOnInit() {
    // for(let i = 1; i<=10; i++) {
    //   this.rooms.push({
    //     name: 'Room name #'+i,
    //     description: 'Room description #'+i,
    //     actualPlayers: 5+i,
    //     maxPlayers: 11+i,
    //     minChips: 500*i
    //   });
    // }
  }

  connect(room: RoomResponse) {
    sessionStorage.setItem('room-'+room.id_room, JSON.stringify(room));
    this.router.navigate(['/room', room.id_room]);
  }

  

  tab : any = 'tab1';
  Clicked : boolean;

    onClick(check){
    //    console.log(check);
        if(check==1){
          this.tab = 'tab1';
        }else if(check==2){
          this.tab = 'tab2';
        }else{
          this.tab = 'tab3';
        }    
      
    }

   onSwitch(check){
 
     switch (check) {
      case 1:
        return 'tab1';
      case 2:
        return 'tab2';
      case 3:
        return 'tab3';
    }
  }


}
