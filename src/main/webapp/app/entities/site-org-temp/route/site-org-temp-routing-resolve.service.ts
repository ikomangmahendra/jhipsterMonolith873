import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISiteOrgTemp } from '../site-org-temp.model';
import { SiteOrgTempService } from '../service/site-org-temp.service';

const siteOrgTempResolve = (route: ActivatedRouteSnapshot): Observable<null | ISiteOrgTemp> => {
  const id = route.params.id;
  if (id) {
    return inject(SiteOrgTempService)
      .find(id)
      .pipe(
        mergeMap((siteOrgTemp: HttpResponse<ISiteOrgTemp>) => {
          if (siteOrgTemp.body) {
            return of(siteOrgTemp.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default siteOrgTempResolve;
