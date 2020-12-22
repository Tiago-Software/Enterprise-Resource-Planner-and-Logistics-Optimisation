import { NgModule } from '@angular/core';
import { UIRoutingModule } from './ui.routing';
import { SharedModule } from 'src/app/shared/shared.module';
import { UiComponent } from './ui.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';

@NgModule({
  declarations: [UiComponent],
  imports: [
    SharedModule,
    UIRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    LayoutContainersModule
  ]
})
export class UiModule { }
