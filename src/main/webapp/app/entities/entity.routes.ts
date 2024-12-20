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
    path: 'site-organisation',
    data: { pageTitle: 'SiteOrganisations' },
    loadChildren: () => import('./site-organisation/site-organisation.routes'),
  },
  {
    path: 'user-site-organisation',
    data: { pageTitle: 'UserSiteOrganisations' },
    loadChildren: () => import('./user-site-organisation/user-site-organisation.routes'),
  },
  {
    path: 'role',
    data: { pageTitle: 'Roles' },
    loadChildren: () => import('./role/role.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
