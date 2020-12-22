import { AuthGuard } from './../shared/auth.guard';
import { AppService } from './../shared/app.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ViewsComponent } from './views.component';
import { ViewRoutingModule } from './views.routing';
import { SharedModule } from '../shared/shared.module';
import { FormControl, FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';


@NgModule({
  declarations: [ViewsComponent],
  providers: [AppService, AuthGuard],
  imports: [
    CommonModule,
    ViewRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    SharedModule
  ]
})
export class ViewsModule { }
