import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MazoComponent } from './mazo.component';

describe('MazoComponent', () => {
  let component: MazoComponent;
  let fixture: ComponentFixture<MazoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MazoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MazoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
