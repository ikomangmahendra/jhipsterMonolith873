import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserTempResolve from './route/user-temp-routing-resolve.service';

const userTempRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-temp.component').then(m => m.UserTempComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-temp-detail.component').then(m => m.UserTempDetailComponent),
    resolve: {
      userTemp: UserTempResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-temp-update.component').then(m => m.UserTempUpdateComponent),
    resolve: {
      userTemp: UserTempResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-temp-update.component').then(m => m.UserTempUpdateComponent),
    resolve: {
      userTemp: UserTempResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userTempRoute;
