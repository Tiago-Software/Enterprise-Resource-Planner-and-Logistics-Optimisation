import { environment } from './../../../environments/environment.prod';
import { AppService } from './../../shared/app.service';
import { AuthGuard } from './../../shared/auth.guard';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InformationComponent } from './information/information.component';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing';
import { SharedModule } from 'src/app/shared/shared.module';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';
import { AgmCoreModule } from '@agm/core';
import { MatGoogleMapsAutocompleteModule } from '@angular-material-extensions/google-maps-autocomplete';

@NgModule({
  declarations: [InformationComponent, AppComponent],
  providers: [AppService, AuthGuard],
  imports: [
    CommonModule,
    AppRoutingModule,
    SharedModule,
    LayoutContainersModule,
    AgmCoreModule.forRoot({
      apiKey: environment.googleApi,
      libraries: ["places"]
    }),
    MatGoogleMapsAutocompleteModule
  ]
})
export class AppModule { }

