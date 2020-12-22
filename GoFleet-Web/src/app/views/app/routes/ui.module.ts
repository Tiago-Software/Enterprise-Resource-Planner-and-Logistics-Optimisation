import { NgModule } from '@angular/core';
import { UIRoutingModule } from './ui.routing';
import { SharedModule } from 'src/app/shared/shared.module';
import { UiComponent } from './ui.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';
import { DashboardsContainersModule } from 'src/app/containers/dashboards/dashboards.containers.module';
import { ComponentsCardsModule } from 'src/app/components/cards/components.cards.module';

@NgModule({
  declarations: [UiComponent],
  imports: [
    SharedModule,
    UIRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    DashboardsContainersModule,
    ComponentsCardsModule,
    LayoutContainersModule
  ]
})
export class UiModule { }

