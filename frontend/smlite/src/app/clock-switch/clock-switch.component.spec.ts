import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ClockSwitchComponent} from './clock-switch.component';

describe('ClockSwitchComponent', () => {
  let component: ClockSwitchComponent;
  let fixture: ComponentFixture<ClockSwitchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ClockSwitchComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClockSwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
