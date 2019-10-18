import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LockSwitchComponent} from './lock-switch.component';

describe('LockSwitchComponent', () => {
  let component: LockSwitchComponent;
  let fixture: ComponentFixture<LockSwitchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LockSwitchComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LockSwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
