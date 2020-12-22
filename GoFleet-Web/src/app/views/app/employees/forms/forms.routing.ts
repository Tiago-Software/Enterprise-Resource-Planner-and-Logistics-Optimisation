import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsComponent } from './forms.component';
import { LayoutsComponent } from './layouts/layouts.component';
import { ComponentsComponent } from './components/components.component';
import { ValidationsComponent } from './validations/validations.component';
import { WizardComponent } from './wizard/wizard.component';
import { SubmitReportComponent } from './submit-report/submit-report.component';
import { RegisterEmployeeComponent } from './register-employee/register-employee.component';

const routes: Routes = [
    {
        path: '', component: FormsComponent,
        children: [
            { path: '', redirectTo: 'submit-report', pathMatch: 'full' },
            { path: 'layouts', component: LayoutsComponent },
            { path: 'components', component: ComponentsComponent },
            { path: 'validations', component: ValidationsComponent },
            { path: 'wizard', component: WizardComponent },
            { path: 'submit-report', component: SubmitReportComponent },
            { path: 'register-employee', component: RegisterEmployeeComponent }
       ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class FormsRoutingModule { }
