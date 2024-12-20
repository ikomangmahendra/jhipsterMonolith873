import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserSiteOrganisationResolve from './route/user-site-organisation-routing-resolve.service';

const userSiteOrganisationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-site-organisation.component').then(m => m.UserSiteOrganisationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-site-organisation-detail.component').then(m => m.UserSiteOrganisationDetailComponent),
    resolve: {
      userSiteOrganisation: UserSiteOrganisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-site-organisation-update.component').then(m => m.UserSiteOrganisationUpdateComponent),
    resolve: {
      userSiteOrganisation: UserSiteOrganisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-site-organisation-update.component').then(m => m.UserSiteOrganisationUpdateComponent),
    resolve: {
      userSiteOrganisation: UserSiteOrganisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userSiteOrganisationRoute;
