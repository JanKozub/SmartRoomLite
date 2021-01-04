import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {ToggleSwitchComponent} from './components/toggle-switch/toggle-switch.component';
import {SwitchesPageComponent} from './pages/switches-page/switches-page.component';
import {ControlPageComponent} from './pages/control-page/control-page.component';
import {SettingsPageComponent} from './pages/settings-page/settings-page.component';
import {RouterModule, Routes} from '@angular/router';
import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {DoubleToggleSwitchComponent} from './components/double-toggle-switch/double-toggle-switch.component';
import {VerticalControllerComponent} from './components/vertical-controller/vertical-controller.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

const appRoutes: Routes = [
  {path: 'switches', component: SwitchesPageComponent},
  {path: 'control', component: ControlPageComponent},
  {path: 'settings', component: SettingsPageComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'switches'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes),
    RouterModule,
    BrowserModule,
    NgxMaterialTimepickerModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    FormsModule,
    ServiceWorkerModule.register("ngsw-worker.js", {enabled: environment.production})
  ],
  declarations: [
    AppComponent,
    ToggleSwitchComponent,
    SwitchesPageComponent,
    ControlPageComponent,
    VerticalControllerComponent,
    DoubleToggleSwitchComponent,
    SettingsPageComponent
  ],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule {
}
