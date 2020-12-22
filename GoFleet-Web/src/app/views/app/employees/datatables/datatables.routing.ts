import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DatatablesComponent } from './datatables.component';
import { FullpageComponent } from './fullpage/fullpage.component';
import { ScrollableComponent } from './scrollable/scrollable.component';
import { ViewReportComponent } from './view-report/view-report.component';

const routes: Routes = [
    {
        path: '', component: DatatablesComponent,
        children: [
            { path: '', redirectTo: 'view-report', pathMatch: 'full' },
            { path: 'fullpage', component: FullpageComponent },
            { path: 'scrollable', component: ScrollableComponent },
            { path: 'view-report', component: ViewReportComponent }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DatatablesRoutingModule { }


