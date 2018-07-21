import { Component } from '@angular/core';

// libs
import { AppBaseComponent, AppService } from '@xplat/nativescript';

@Component({
  selector: 'pkc-root',
  template: `
    <StackLayout class="page p-20">
      <Label automationText="helloLabel" [text]="'hello' | translate" class="h1 p-10 text-center"></Label>
    </StackLayout>`
})
export class AppComponent extends AppBaseComponent {

  constructor(
    appService: AppService
  ) {
    super(appService);
  }
}
