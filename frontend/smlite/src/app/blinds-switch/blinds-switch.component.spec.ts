import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BlindsSwitchComponent} from './blinds-switch.component';

describe('BlindsSwitchComponent', () => {
  let component: BlindsSwitchComponent;
  let fixture: ComponentFixture<BlindsSwitchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BlindsSwitchComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlindsSwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
