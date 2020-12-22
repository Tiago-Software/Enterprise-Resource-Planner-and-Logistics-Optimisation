import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComponentsComponent } from './components/components.component';
import { LayoutsComponent } from './layouts/layouts.component';
import { ValidationsComponent } from './validations/validations.component';
import { FormsComponent } from './forms.component';
import { FormsRoutingModule } from './forms.routing';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsContainersModule } from 'src/app/containers/forms/forms.containers.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { ComponentsCarouselModule } from 'src/app/components/carousel/components.carousel.module';

import { PagesContainersModule } from 'src/app/containers/pages/pages.containers.module';
import { FormsModule as FormsModuleAngular, ReactiveFormsModule } from '@angular/forms';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { RatingModule } from 'ngx-bootstrap/rating';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { WizardComponent } from './wizard/wizard.component';
import { FormValidationsContainersModule } from 'src/app/containers/form-validations/form.validations.containers.module';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';
import { WizardsContainersModule } from 'src/app/containers/wizard/wizards.containers.module';


import { BootstrapModule } from './../../../../components/bootstrap/bootstrap.module';

@NgModule({
  declarations: [ComponentsComponent, LayoutsComponent, ValidationsComponent, FormsComponent, WizardComponent],
  imports: [
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
    WizardsContainersModule,
    PaginationModule.forRoot(),
    TabsModule.forRoot(),
    ModalModule.forRoot(),
    BsDropdownModule.forRoot(),
    AccordionModule.forRoot(),
    RatingModule.forRoot()
  ]
})
export class FormsModule { }
