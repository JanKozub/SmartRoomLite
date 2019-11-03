import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {LightSwitchComponent} from '../components/switches/light-switch/light-switch.component';
import {ClockSwitchComponent} from '../components/switches/clock-switch/clock-switch.component';
import {LockSwitchComponent} from '../components/switches/lock-switch/lock-switch.component';
import {MusicSwitchComponent} from '../components/switches/music-switch/music-switch.component';
import {BlindsSwitchComponent} from '../components/switches/blinds-switch/blinds-switch.component';
import {SettingsSwitchComponent} from '../components/switches/settings-switch/settings-switch.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {VideoSwitchComponent} from '../components/switches/video-switch/video-switch.component';
import {RouterModule} from "@angular/router";
import {BlindsPageComponent} from '../pages/blinds-page/blinds-page.component';
import {MainPageComponent} from '../pages/main-page/main-page.component';
import {MusicPageComponent} from '../pages/music-page/music-page.component';
import {VideoPageComponent} from '../pages/video-page/video-page.component';
import {SettingsPageComponent} from '../pages/settings-page/settings-page.component';
import {NavBackComponent} from '../components/nav-back/nav-back.component';

@NgModule({
  declarations: [
    AppComponent,
    LightSwitchComponent,
    ClockSwitchComponent,
    LockSwitchComponent,
    MusicSwitchComponent,
    BlindsSwitchComponent,
    SettingsSwitchComponent,
    VideoSwitchComponent,
    BlindsPageComponent,
    MainPageComponent,
    MusicPageComponent,
    VideoPageComponent,
    SettingsPageComponent,
    NavBackComponent
  ],
  imports: [BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}), RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
