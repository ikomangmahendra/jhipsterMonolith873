import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SiteOrgTempResolve from './route/site-org-temp-routing-resolve.service';

const siteOrgTempRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/site-org-temp.component').then(m => m.SiteOrgTempComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/site-org-temp-detail.component').then(m => m.SiteOrgTempDetailComponent),
    resolve: {
      siteOrgTemp: SiteOrgTempResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/site-org-temp-update.component').then(m => m.SiteOrgTempUpdateComponent),
    resolve: {
      siteOrgTemp: SiteOrgTempResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default siteOrgTempRoute;
