import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { InformationComponent } from './information/information.component';

const routes: Routes = [
    {
        path: '', component: AppComponent,
        children: [
            { path: '', pathMatch: 'full', redirectTo: 'routes' },
            { path: 'dashboards', loadChildren: () => import('./dashboards/dashboards.module').then(m => m.DashboardsModule) },
            { path: 'applications', loadChildren: () => import('./applications/applications.module').then(m => m.ApplicationsModule) },
            { path: 'pages', loadChildren: () => import('./pages/pages.module').then(m => m.PagesModule) },
            { path: 'routes', loadChildren: () => import('./routes/ui.module').then(m => m.UiModule) },
            { path: 'ui', loadChildren: () => import('./ui/ui.module').then(m => m.UiModule) },
            { path: 'orders', loadChildren: () => import('./orders/ui.module').then(m => m.UiModule) },
            { path: 'employees', loadChildren: () => import('./employees/ui.module').then(m => m.UiModule) },
            { path: 'fleet', loadChildren: () => import('./fleet/ui.module').then(m => m.UiModule) },
            { path: 'stock', loadChildren: () => import('./stock/ui.module').then(m => m.UiModule) },
            { path: 'branch', loadChildren: () => import('./branch/ui.module').then(m => m.UiModule) },
            { path: 'menu', loadChildren: () => import('./menu/menu.module').then(m => m.MenuModule) },
            { path: 'information', component: InformationComponent },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
