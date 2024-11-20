import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ConsumerResolve from './route/consumer-routing-resolve.service';

const consumerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/consumer.component').then(m => m.ConsumerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/consumer-detail.component').then(m => m.ConsumerDetailComponent),
    resolve: {
      consumer: ConsumerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/consumer-update.component').then(m => m.ConsumerUpdateComponent),
    resolve: {
      consumer: ConsumerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/consumer-update.component').then(m => m.ConsumerUpdateComponent),
    resolve: {
      consumer: ConsumerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consumerRoute;
