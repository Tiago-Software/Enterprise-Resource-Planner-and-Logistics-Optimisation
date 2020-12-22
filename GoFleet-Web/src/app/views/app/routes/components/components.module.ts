import { environment } from './../../../../../environments/environment.prod';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsComponent } from './analytics/analytics.component';
import { MapsComponent } from './maps/maps.component';
import { ComponentsComponent } from './components.component';
import { ComponentsRoutingModule } from './components.routing';
import { SharedModule } from 'src/app/shared/shared.module';
import { ComponentsChartModule } from 'src/app/components/charts/components.charts.module';
import { ComponentsCarouselModule } from 'src/app/components/carousel/components.carousel.module';
import { FormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill';
import { ComponentsStateButtonModule } from 'src/app/components/state-button/components.state-button.module';
import { BootstrapModule } from 'src/app/components/bootstrap/bootstrap.module';
import { UiCardsContainersModule } from 'src/app/containers/ui/cards/ui.cards.containers.module';
import { UiModalsContainersModule } from 'src/app/containers/ui/modals/ui.modals.containers.module';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';
import { DashboardsContainersModule } from 'src/app/containers/dashboards/dashboards.containers.module';
import { ComponentsCardsModule } from 'src/app/components/cards/components.cards.module';
import { AgmCoreModule } from '@agm/core';
import {GoogleMapsModule} from '@angular/google-maps';
import {AgmDirectionModule} from 'agm-direction';
import { FormsModule as FormsModuleAngular, ReactiveFormsModule } from '@angular/forms';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { PagesContainersModule } from 'src/app/containers/pages/pages.containers.module';

@NgModule({
  declarations: [
    AnalyticsComponent,
    MapsComponent,
    ComponentsComponent
  ],
  imports: [
    PagesContainersModule,
    NgxDatatableModule,
    ReactiveFormsModule,
    FormsModuleAngular,
    GoogleMapsModule,
    AgmDirectionModule,
    CommonModule,
    SharedModule,
    ComponentsRoutingModule,
    DashboardsContainersModule,
    UiCardsContainersModule,
    ComponentsChartModule,
    ComponentsCarouselModule,
    FormsModule,
    UiModalsContainersModule,
    QuillModule.forRoot(),
    AgmCoreModule.forRoot({
      apiKey: environment.googleApi
    }),
    LayoutContainersModule,
    ComponentsCardsModule,
    ComponentsStateButtonModule,
    BootstrapModule
  ]
})
export class ComponentsModule { }


