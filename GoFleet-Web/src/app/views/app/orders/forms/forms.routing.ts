import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsComponent } from './forms.component';
import { orderDetailsComponent } from './orderDetails/orderDetails.component';
import { SubmitOrderComponent } from './submit-order/submit-order.component';
import { ComponentsComponent } from './components/components.component';
import { ValidationsComponent } from './validations/validations.component';
import { WizardComponents } from './wizard/wizard.component';

const routes: Routes = [
    {
        path: '', component: FormsComponent,
        children: [
            { path: '', redirectTo: 'submit-order', pathMatch: 'full' },
            { path: 'submit-order', component: SubmitOrderComponent },
            { path: 'orderDetails/:id', component: orderDetailsComponent },
            { path: 'components', component: ComponentsComponent },
            { path: 'validations', component: ValidationsComponent },
            { path: 'wizard', component: WizardComponents },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class FormsRoutingModule { }
