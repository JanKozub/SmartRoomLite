import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MusicSwitchComponent} from './music-switch.component';

describe('MusicSwitchComponent', () => {
  let component: MusicSwitchComponent;
  let fixture: ComponentFixture<MusicSwitchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MusicSwitchComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MusicSwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
