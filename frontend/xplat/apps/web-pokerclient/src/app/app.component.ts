import { Component } from '@angular/core';

// xplat
import { AppBaseComponent } from '@xplat/web';

@Component({
  selector: 'pkc-root',
  templateUrl: './app.component.html'
})
export class AppComponent extends AppBaseComponent {
  constructor() {
    super();
  }
}
