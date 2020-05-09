import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionMenuFriendsComponent } from './action-menu-friends.component';

describe('ActionMenuFriendsComponent', () => {
  let component: ActionMenuFriendsComponent;
  let fixture: ComponentFixture<ActionMenuFriendsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActionMenuFriendsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionMenuFriendsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
