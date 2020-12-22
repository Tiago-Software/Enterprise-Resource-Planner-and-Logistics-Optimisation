import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationsComponent } from './applications.component';
import { TodoComponent } from './todo/todo.component';
import { SurveyComponent } from './survey/survey.component';
import { SurveyDetailComponent } from './survey-detail/survey-detail.component';
import { ChatComponent } from './chat/chat.component';
import { ApplicationsRoutingModule } from './applications.routing';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { ApplicationsContainersModule } from 'src/app/containers/applications/applications.containers.module';
import { ComponentsChartModule } from 'src/app/components/charts/components.charts.module';
import { LayoutContainersModule } from 'src/app/containers/layout/layout.containers.module';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ProgressbarModule } from 'ngx-bootstrap/progressbar';
import { ModalModule } from 'ngx-bootstrap/modal';

@NgModule({
  declarations: [ApplicationsComponent, TodoComponent, SurveyComponent, SurveyDetailComponent, ChatComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    ApplicationsRoutingModule,
    LayoutContainersModule,
    ApplicationsContainersModule,
    ComponentsChartModule,
    CollapseModule.forRoot(),
    TabsModule.forRoot(),
    ProgressbarModule.forRoot(),
    ModalModule.forRoot()
  ]
})
export class ApplicationsModule { }
