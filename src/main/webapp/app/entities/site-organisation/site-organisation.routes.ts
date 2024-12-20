import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SiteOrganisationResolve from './route/site-organisation-routing-resolve.service';

const siteOrganisationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/site-organisation.component').then(m => m.SiteOrganisationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/site-organisation-detail.component').then(m => m.SiteOrganisationDetailComponent),
    resolve: {
      siteOrganisation: SiteOrganisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/site-organisation-update.component').then(m => m.SiteOrganisationUpdateComponent),
    resolve: {
      siteOrganisation: SiteOrganisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default siteOrganisationRoute;
