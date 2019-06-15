import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StackerComponent } from './stacker.component';

describe('StackerComponent', () => {
  let component: StackerComponent;
  let fixture: ComponentFixture<StackerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StackerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StackerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
