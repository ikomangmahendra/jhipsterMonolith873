import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserTemp } from '../user-temp.model';
import { UserTempService } from '../service/user-temp.service';

const userTempResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserTemp> => {
  const id = route.params.id;
  if (id) {
    return inject(UserTempService)
      .find(id)
      .pipe(
        mergeMap((userTemp: HttpResponse<IUserTemp>) => {
          if (userTemp.body) {
            return of(userTemp.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userTempResolve;
