import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

  public tab = 1;
  @Input() rooms: RoomResponse[];
  @Output() reload: EventEmitter<() => void> = new EventEmitter<() => void>();
  public loading: boolean;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  connect(room: RoomResponse) {
    localStorage.setItem('room-' + room.id_room, JSON.stringify(room));
    // this.router.navigate(['/room', room.id_room]);
    window.open('#/room/' + room.id_room, '_blank');
  }

  reloading() {
    this.loading = true;
    this.reload.emit(() => {
      this.loading = false;
    });
  }

  roomsLenght(){
    if (this.rooms){
      return this.rooms.length
    } else {
      return 0
    }
  }

}
