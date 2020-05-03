import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-stacker',
  templateUrl: './stacker.component.html',
  styleUrls: ['./stacker.component.scss']
})
export class StackerComponent implements OnInit, OnChanges {

  @Input() valor: number;
  chipsStacked: string[];

  constructor() { }

  ngOnInit() {
    this.recalcChips();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.recalcChips();
  }

  recalcChips() {
    this.chipsStacked = [];
    const chipsToStack = [];
    /*
      values:
      10
      25
      100
      250
      500
       1k
       2K
       5K
       10K
       25k
       50k
       100k
       250k
       500k
       1M
       5M
       5M
    */
    let fichas = this.valor;

    const M10 = fichas / 10000000;
    if (M10 >= 1) {
      const cant = Math.floor(M10);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('10M');
      }
      fichas = fichas % 10000000;
    }

    const M5 = fichas / 5000000;
    if (M5 >= 1) {
      const cant = Math.floor(M5);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('5M');
      }
      fichas = fichas % 5000000;
    }

    // const M2 = fichas / 2000000;
    // if (M2 >= 1) {
    //   const cant = Math.floor(M2);
    //   for (let i = 0; i < cant; i++) {
    //     this.chipsStacked.unshift('2M');
    //   }
    //   fichas = fichas % 2000000;
    // }

    const M1 = fichas / 1000000;
    if (M1 >= 1) {
      const cant = Math.floor(M1);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('1M');
      }
      fichas = fichas % 1000000;
    }

    const K500 = fichas / 500000;
    if (K500 >= 1) {
      const cant = Math.floor(K500);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('500K');
      }
      fichas = fichas % 500000;
    }

    const K250 = fichas / 250000;
    if (K250 >= 1) {
      const cant = Math.floor(K250);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('250K');
      }
      fichas = fichas % 250000;
    }

    const K100 = fichas / 100000;
    if (K100 >= 1) {
      const cant = Math.floor(K100);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('100K');
      }
      fichas = fichas % 100000;
    }

    const K50 = fichas / 50000;
    if (K50 >= 1) {
      const cant = Math.floor(K50);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('50K');
      }
      fichas = fichas % 50000;
    }

    const K25 = fichas / 25000;
    if (K25 >= 1) {
      const cant = Math.floor(K25);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('25K');
      }
      fichas = fichas % 25000;
    }

    const K10 = fichas / 10000;
    if (K10 >= 1) {
      const cant = Math.floor(K10);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('10K');
      }
      fichas = fichas % 10000;
    }

    const K5 = fichas / 5000;
    if (K5 >= 1) {
      const cant = Math.floor(K5);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('5K');
      }
      fichas = fichas % 5000;
    }

    const K2 = fichas / 2000;
    if (K2 >= 1) {
      const cant = Math.floor(K2);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('2K');
      }
      fichas = fichas % 2000;
    }

    const K1 = fichas / 1000;
    if (K1 >= 1) {
      const cant = Math.floor(K1);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('1K');
      }
      fichas = fichas % 1000;
    }

    const v500 = fichas / 500;
    if (v500 >= 1) {
      const cant = Math.floor(v500);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('500');
      }
      fichas = fichas % 500;
    }

    const v250 = fichas / 250;
    if (v250 >= 1) {
      const cant = Math.floor(v250);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('250');
      }
      fichas = fichas % 250;
    }

    const v100 = fichas / 100;
    if (v100 >= 1) {
      const cant = Math.floor(v100);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('100');
      }
      fichas = fichas % 100;
    }

    const v50 = fichas / 50;
    if (v50 >= 1) {
      const cant = Math.floor(v50);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('50');
      }
      fichas = fichas % 50;
    }

    const v25 = fichas / 25;
    if (v25 >= 1) {
      const cant = Math.floor(v25);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('25');
      }
      fichas = fichas % 25;
    }

    const v10 = fichas / 10;
    if (v10 >= 1) {
      const cant = Math.floor(v10);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('10');
      }
      fichas = fichas % 10;
    }

    const v1 = fichas / 1;
    if (v1 >= 1) {
      const cant = Math.floor(v1);
      for (let i = 0; i < cant; i++) {
        chipsToStack.unshift('1');
      }
    }
    // console.log('Real stacked chips', chipsToStack);
    this.chipsStacked = chipsToStack.slice(chipsToStack.length - 25, chipsToStack.length);
  }

}
