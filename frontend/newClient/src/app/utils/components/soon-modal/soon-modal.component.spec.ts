import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SoonModalComponent } from './soon-modal.component';

describe('SoonModalComponent', () => {
  let component: SoonModalComponent;
  let fixture: ComponentFixture<SoonModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SoonModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SoonModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
