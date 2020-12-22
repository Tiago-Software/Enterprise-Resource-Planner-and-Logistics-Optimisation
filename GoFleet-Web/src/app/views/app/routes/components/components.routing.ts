import { AnalyticsComponent } from './analytics/analytics.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ComponentsComponent } from './components.component';
import { MapsComponent } from './maps/maps.component';

const routes: Routes = [
    {
        path: '', component: ComponentsComponent,
        children: [
            { path: '', redirectTo: 'maps', pathMatch: 'full' },
            { path: 'maps', component: MapsComponent },
            { path: 'analytics', component: AnalyticsComponent }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ComponentsRoutingModule { }
