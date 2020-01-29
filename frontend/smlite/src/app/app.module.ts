import '../polyfills';

import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from './material-module';
import '@vaadin/vaadin-icons';
import {ToggleSwitchComponent} from './components/toggle-switch/toggle-switch.component';
import {SwitchesPageComponent} from './pages/switches-page/switches-page.component';
import {ControlPageComponent} from './pages/control-page/control-page.component';
import {MusicPageComponent} from './pages/music-page/music-page.component';
import {SettingsPageComponent} from './pages/settings-page/settings-page.component';
import {DoubleToggleSwitchComponent} from './components/double-toggle-switch/double-toggle-switch.component';
import {VerticalControllerComponent} from './components/vertical-controller/vertical-controller.component';

const appRoutes: Routes = [
  {path: 'switches', component: SwitchesPageComponent},
  {path: 'control', component: ControlPageComponent},
  {path: 'music', component: MusicPageComponent},
  {path: 'settings', component: SettingsPageComponent},
  {path: '**', pathMatch: 'full', redirectTo: '/switches'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: true}
    ),
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    MaterialModule,
    RouterModule],
  declarations: [
    AppComponent,
    ToggleSwitchComponent,
    SwitchesPageComponent,
    ControlPageComponent,
    MusicPageComponent,
    SettingsPageComponent,
    DoubleToggleSwitchComponent,
    VerticalControllerComponent
  ],
  entryComponents: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
  providers: []
})
export class AppModule {
}
