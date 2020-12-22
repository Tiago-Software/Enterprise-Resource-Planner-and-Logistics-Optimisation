import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { DatatablesComponent } from './datatables.component';
import { FullpageComponent } from './fullpage/fullpage.component';
import { ScrollableComponent } from './scrollable/scrollable.component';
import { DatatablesRoutingModule } from './datatables.routing';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { PagesContainersModule } from 'src/app/containers/pages/pages.containers.module';
import { ViewOrderComponent } from './view-order/view-order.component';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';
import { PaginationModule } from 'ngx-bootstrap/pagination';

@NgModule({
  declarations: [DatatablesComponent, FullpageComponent, ScrollableComponent, ViewOrderComponent],
  imports: [
    CommonModule,
    SharedModule,
    DatatablesRoutingModule,
    LayoutContainersModule,
    NgxDatatableModule,
    CollapseModule,
    PagesContainersModule,
    TabsModule.forRoot(),
    PaginationModule.forRoot()
  ]
})
export class DatatablesModule { }
