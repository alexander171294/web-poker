import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserMenuActionsComponent } from './user-menu-actions.component';

describe('UserMenuActionsComponent', () => {
  let component: UserMenuActionsComponent;
  let fixture: ComponentFixture<UserMenuActionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserMenuActionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserMenuActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
