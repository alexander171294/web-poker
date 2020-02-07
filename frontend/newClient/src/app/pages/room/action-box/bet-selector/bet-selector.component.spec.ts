import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BetSelectorComponent } from './bet-selector.component';

describe('BetSelectorComponent', () => {
  let component: BetSelectorComponent;
  let fixture: ComponentFixture<BetSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BetSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BetSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
