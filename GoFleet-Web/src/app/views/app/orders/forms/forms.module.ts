import { environment } from './../../../../../environments/environment.prod';
import { orderDetailsComponent } from './orderDetails/orderDetails.component';
import { ArchwizardModule } from 'angular-archwizard';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubmitOrderComponent } from './submit-order/submit-order.component';
import { ComponentsComponent } from './components/components.component';
import { ValidationsComponent } from './validations/validations.component';
import { FormsComponent } from './forms.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { FormsRoutingModule } from './forms.routing';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsContainersModule } from 'src/app/containers/forms/forms.containers.module';
import { NgSelectModule } from '@ng-select/ng-select';
//GOOGLE MAPS
import { GooglePlaceModule } from "ngx-google-places-autocomplete";
import { AgmCoreModule } from '@agm/core';
import {GoogleMapsModule} from '@angular/google-maps';
import {AgmDirectionModule} from 'agm-direction';

import { PagesContainersModule } from 'src/app/containers/pages/pages.containers.module';
import { FormsModule as FormsModuleAngular, ReactiveFormsModule } from '@angular/forms';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { RatingModule } from 'ngx-bootstrap/rating';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { WizardComponents } from './wizard/wizard.component';
import { FormValidationsContainersModule } from 'src/app/containers/form-validations/form.validations.containers.module';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';
import { WizardsContainersModule } from 'src/app/containers/wizard/wizards.containers.module';

import { MatGoogleMapsAutocompleteModule } from '@angular-material-extensions/google-maps-autocomplete';

import { BootstrapModule } from './../../../../components/bootstrap/bootstrap.module';

//orderdetails

import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';
import { ComponentsCarouselModule } from 'src/app/components/carousel/components.carousel.module';

import {MatStepperModule} from '@angular/material/stepper';
import {MatIconModule} from '@angular/material/icon';


@NgModule({
  declarations: [ComponentsComponent, SubmitOrderComponent, ValidationsComponent, FormsComponent, WizardComponents, orderDetailsComponent],
  imports: [
    MatIconModule,
    MatStepperModule,
    ArchwizardModule,
    ComponentsCarouselModule,
    GooglePlaceModule,
    NgxDatatableModule,
    PagesContainersModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    FormsRoutingModule,
    FormsContainersModule,
    FormsModuleAngular,
    FormValidationsContainersModule,
    NgSelectModule,
    BsDatepickerModule,
    LayoutContainersModule,
    MatGoogleMapsAutocompleteModule,
    WizardsContainersModule,
    PaginationModule.forRoot(),
    TabsModule.forRoot(),
    ModalModule.forRoot(),
    BsDropdownModule.forRoot(),
    AccordionModule.forRoot(),
    RatingModule.forRoot(),
    GoogleMapsModule,
    AgmDirectionModule,
    AgmCoreModule.forRoot({
      apiKey: environment.googleApi,
      libraries: ['geometry','places']
    })
  ]
})
export class FormsModule { }
