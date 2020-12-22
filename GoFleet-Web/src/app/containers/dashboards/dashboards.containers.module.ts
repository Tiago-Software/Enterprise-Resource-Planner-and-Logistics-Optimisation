import { NgModule, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvancedSearchComponent } from './advanced-search/advanced-search.component';
import { BestSellersComponent } from './best-sellers/best-sellers.component';
import { CakesComponent } from './cakes/cakes.component';
import { CalendarComponent } from './calendar/calendar.component';
import { ConversionRatesChartCardComponent } from './conversion-rates-chart-card/conversion-rates-chart-card.component';
import { GradientCardContainerComponent } from './gradient-card-container/gradient-card-container.component';
import { IconCardsCarouselComponent } from './icon-cards-carousel/icon-cards-carousel.component';
import { LogsComponent } from './logs/logs.component';
import { NewCommentsComponent } from './new-comments/new-comments.component';
import { OrderStockRadarChartComponent } from './order-stock-radar-chart/order-stock-radar-chart.component';
import { ProductCategoriesDoughnutComponent } from './product-categories-doughnut/product-categories-doughnut.component';
import { ProductCategoriesPolarAreaComponent } from './product-categories-polar-area/product-categories-polar-area.component';
import { ProfileStatusesComponent } from './profile-statuses/profile-statuses.component';
import { QuickPostComponent } from './quick-post/quick-post.component';
import { RecentOrdersComponent } from './recent-orders/recent-orders.component';
import { SalesChartCardComponent } from './sales-chart-card/sales-chart-card.component';
import { SmallLineChartsComponent } from './small-line-charts/small-line-charts.component';
import { SortableStatisticsRowComponent } from './sortable-statistics-row/sortable-statistics-row.component';
import { TicketsComponent } from './tickets/tickets.component';
import { TopRatedItemsComponent } from './top-rated-items/top-rated-items.component';
import { WebsiteVisitsChartCardComponent } from './website-visits-chart-card/website-visits-chart-card.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ComponentsCarouselModule } from 'src/app/components/carousel/components.carousel.module';
import { ComponentsChartModule } from 'src/app/components/charts/components.charts.module';
import { CalendarModule, DateAdapter, CalendarNativeDateFormatter, DateFormatterParams, CalendarDateFormatter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { ComponentsCardsModule } from 'src/app/components/cards/components.cards.module';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { FormsModule  } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { RatingModule } from 'ngx-bootstrap/rating';
import { ProgressbarModule } from 'ngx-bootstrap/progressbar';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';

@Injectable()
export class CustomDateFormatter extends CalendarNativeDateFormatter {
  public monthViewColumnHeader({date, locale}: DateFormatterParams): string {
    return new Intl.DateTimeFormat(locale, {weekday: 'short'}).format(date);
  }
}

@NgModule({
    declarations: [
        AdvancedSearchComponent,
        BestSellersComponent,
        CakesComponent,
        CalendarComponent,
        ConversionRatesChartCardComponent,
        GradientCardContainerComponent,
        IconCardsCarouselComponent,
        LogsComponent,
        NewCommentsComponent,
        OrderStockRadarChartComponent,
        ProductCategoriesDoughnutComponent,
        ProductCategoriesPolarAreaComponent,
        ProfileStatusesComponent,
        QuickPostComponent,
        RecentOrdersComponent,
        SalesChartCardComponent,
        SmallLineChartsComponent,
        SortableStatisticsRowComponent,
        TicketsComponent,
        TopRatedItemsComponent,
        WebsiteVisitsChartCardComponent,

    ],
    imports: [
        SharedModule,
        ComponentsCarouselModule,
        ComponentsChartModule,
        CalendarModule.forRoot({
          provide: DateAdapter,
          useFactory: adapterFactory
        }),
        ComponentsCardsModule,
        NgxDatatableModule,
        RatingModule.forRoot(),
        FormsModule,
        NgSelectModule,
        ProgressbarModule.forRoot(),
        ModalModule.forRoot(),
        BsDropdownModule.forRoot()
    ],
    providers: [
      {provide: CalendarDateFormatter, useClass: CustomDateFormatter}
    ],
    exports: [
        AdvancedSearchComponent,
        BestSellersComponent,
        CakesComponent,
        CalendarComponent,
        ConversionRatesChartCardComponent,
        GradientCardContainerComponent,
        IconCardsCarouselComponent,
        LogsComponent,
        NewCommentsComponent,
        OrderStockRadarChartComponent,
        ProductCategoriesDoughnutComponent,
        ProductCategoriesPolarAreaComponent,
        ProfileStatusesComponent,
        QuickPostComponent,
        RecentOrdersComponent,
        SalesChartCardComponent,
        SmallLineChartsComponent,
        SortableStatisticsRowComponent,
        TicketsComponent,
        TopRatedItemsComponent,
        WebsiteVisitsChartCardComponent,
    ]
})

export class DashboardsContainersModule { }
