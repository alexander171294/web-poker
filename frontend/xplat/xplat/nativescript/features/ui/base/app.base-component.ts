import { Component } from '@angular/core';

// libs
import { BaseComponent } from '@xplat/core';
import { AppService } from '@xplat/nativescript/core';

export abstract class AppBaseComponent extends BaseComponent {
  constructor(protected appService: AppService) {
    super();
  }
}
