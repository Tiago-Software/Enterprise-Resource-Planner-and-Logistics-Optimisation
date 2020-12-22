import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsComponent } from './forms.component';
import { NewOrderComponent } from './new-order/new-order.component';
import { ComponentsComponent } from './components/components.component';
import { ValidationsComponent } from './validations/validations.component';
import { WizardComponent } from './wizard/wizard.component';

const routes: Routes = [
    {
        path: '', component: FormsComponent,
        children: [
            { path: '', redirectTo: 'new-order', pathMatch: 'full' },
            { path: 'new-order', component: NewOrderComponent },
            { path: 'components', component: ComponentsComponent },
            { path: 'validations', component: ValidationsComponent },
            { path: 'wizard', component: WizardComponent },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class FormsRoutingModule { }
