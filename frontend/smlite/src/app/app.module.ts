import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {LightSwitchComponent} from '../components/light-switch/light-switch.component';
import {ClockSwitchComponent} from '../components/clock-switch/clock-switch.component';
import {LockSwitchComponent} from '../components/lock-switch/lock-switch.component';
import {MusicSwitchComponent} from '../components/music-switch/music-switch.component';
import {BlindsSwitchComponent} from '../components/blinds-switch/blinds-switch.component';
import {SettingsSwitchComponent} from '../components/settings-switch/settings-switch.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    LightSwitchComponent,
    ClockSwitchComponent,
    LockSwitchComponent,
    MusicSwitchComponent,
    BlindsSwitchComponent,
    SettingsSwitchComponent
  ],
  imports: [BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production})],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
