import { MatFormFieldModule, MatInputModule, MatButtonModule } from '@angular/material';
import { NgModule } from '@angular/core';

const componentes = [
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
];

@NgModule({
  imports: componentes,
  exports: componentes,
})
export class MaterialModule { }
