import '../polyfills';

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
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from "@angular/router";
import {MainPageComponent} from '../pages/main-page/main-page.component';
import {MusicPageComponent} from '../pages/music-page/music-page.component';
import {VideoPageComponent} from '../pages/video-page/video-page.component';
import {SettingsPageComponent} from '../pages/settings-page/settings-page.component';
import {NavBackComponent} from '../components/nav-back/nav-back.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from './material-module';
import {SliderComponent} from '../components/slider/slider.component';
import {NightModeSwitchComponent} from '../components/switches/night-mode-switch/night-mode-switch.component';

@NgModule({
  imports: [BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    MaterialModule,
    RouterModule],
  declarations: [
    AppComponent,
    LightSwitchComponent,
    ClockSwitchComponent,
    LockSwitchComponent,
    MainPageComponent,
    MusicPageComponent,
    VideoPageComponent,
    SettingsPageComponent,
    NavBackComponent,
    SliderComponent,
    NightModeSwitchComponent
  ],
  entryComponents: [SliderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
  providers: []
})
export class AppModule {
}
