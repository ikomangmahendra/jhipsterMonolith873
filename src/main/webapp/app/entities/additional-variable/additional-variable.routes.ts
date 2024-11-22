import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AdditionalVariableResolve from './route/additional-variable-routing-resolve.service';

const additionalVariableRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/additional-variable.component').then(m => m.AdditionalVariableComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/additional-variable-detail.component').then(m => m.AdditionalVariableDetailComponent),
    resolve: {
      additionalVariable: AdditionalVariableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/additional-variable-update.component').then(m => m.AdditionalVariableUpdateComponent),
    resolve: {
      additionalVariable: AdditionalVariableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/additional-variable-update.component').then(m => m.AdditionalVariableUpdateComponent),
    resolve: {
      additionalVariable: AdditionalVariableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default additionalVariableRoute;
