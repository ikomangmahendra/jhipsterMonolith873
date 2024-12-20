import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserSiteOrganisation } from '../user-site-organisation.model';
import { UserSiteOrganisationService } from '../service/user-site-organisation.service';

const userSiteOrganisationResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserSiteOrganisation> => {
  const id = route.params.id;
  if (id) {
    return inject(UserSiteOrganisationService)
      .find(id)
      .pipe(
        mergeMap((userSiteOrganisation: HttpResponse<IUserSiteOrganisation>) => {
          if (userSiteOrganisation.body) {
            return of(userSiteOrganisation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userSiteOrganisationResolve;
