import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProgramVersionResolve from './route/program-version-routing-resolve.service';

const programVersionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/program-version.component').then(m => m.ProgramVersionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/program-version-detail.component').then(m => m.ProgramVersionDetailComponent),
    resolve: {
      programVersion: ProgramVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/program-version-update.component').then(m => m.ProgramVersionUpdateComponent),
    resolve: {
      programVersion: ProgramVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/program-version-update.component').then(m => m.ProgramVersionUpdateComponent),
    resolve: {
      programVersion: ProgramVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default programVersionRoute;
