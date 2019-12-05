import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainPageComponent} from "../pages/main-page/main-page.component";
import {MusicPageComponent} from "../pages/music-page/music-page.component";
import {VideoPageComponent} from "../pages/video-page/video-page.component";
import {SettingsPageComponent} from "../pages/settings-page/settings-page.component";

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: MainPageComponent
  },
  {
    path: 'music',
    component: MusicPageComponent
  },
  {
    path: 'video',
    component: VideoPageComponent
  },
  {
    path: 'settings',
    component: SettingsPageComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      {enableTracing: true}
    )
  ]
})

export class AppRoutingModule {
}
