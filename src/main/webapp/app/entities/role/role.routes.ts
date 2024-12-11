import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RoleResolve from './route/role-routing-resolve.service';

const roleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/role.component').then(m => m.RoleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/role-detail.component').then(m => m.RoleDetailComponent),
    resolve: {
      role: RoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/role-update.component').then(m => m.RoleUpdateComponent),
    resolve: {
      role: RoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roleRoute;