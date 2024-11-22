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
    path: 'site-org-temp',
    data: { pageTitle: 'SiteOrgTemps' },
    loadChildren: () => import('./site-org-temp/site-org-temp.routes'),
  },
  {
    path: 'program',
    data: { pageTitle: 'Programs' },
    loadChildren: () => import('./program/program.routes'),
  },
  {
    path: 'program-version',
    data: { pageTitle: 'ProgramVersions' },
    loadChildren: () => import('./program-version/program-version.routes'),
  },
  {
    path: 'accute-program-variable',
    data: { pageTitle: 'AccuteProgramVariables' },
    loadChildren: () => import('./accute-program-variable/accute-program-variable.routes'),
  },
  {
    path: 'additional-variable',
    data: { pageTitle: 'AdditionalVariables' },
    loadChildren: () => import('./additional-variable/additional-variable.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
