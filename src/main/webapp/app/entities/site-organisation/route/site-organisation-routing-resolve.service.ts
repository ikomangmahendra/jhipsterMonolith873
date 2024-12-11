import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISiteOrganisation } from '../site-organisation.model';
import { SiteOrganisationService } from '../service/site-organisation.service';

const siteOrganisationResolve = (route: ActivatedRouteSnapshot): Observable<null | ISiteOrganisation> => {
  const id = route.params.id;
  if (id) {
    return inject(SiteOrganisationService)
      .find(id)
      .pipe(
        mergeMap((siteOrganisation: HttpResponse<ISiteOrganisation>) => {
          if (siteOrganisation.body) {
            return of(siteOrganisation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default siteOrganisationResolve;
