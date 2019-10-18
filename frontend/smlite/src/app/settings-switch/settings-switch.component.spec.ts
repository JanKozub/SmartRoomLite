import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SettingsSwitchComponent} from './settings-switch.component';

describe('SettingsSwitchComponent', () => {
  let component: SettingsSwitchComponent;
  let fixture: ComponentFixture<SettingsSwitchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SettingsSwitchComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsSwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
