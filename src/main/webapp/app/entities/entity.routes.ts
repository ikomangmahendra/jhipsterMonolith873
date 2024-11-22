import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-temp',
    data: { pageTitle: 'UserTemps' },
    loadChildren: () => import('./user-temp/user-temp.routes'),
  },
  {
    path: 'program',
    data: { pageTitle: 'Programs' },
    loadChildren: () => import('./program/program.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
