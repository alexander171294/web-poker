import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DualStackComponent } from './dual-stack.component';

describe('DualStackComponent', () => {
  let component: DualStackComponent;
  let fixture: ComponentFixture<DualStackComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DualStackComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DualStackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
