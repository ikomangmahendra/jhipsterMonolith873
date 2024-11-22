import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProgramVersion } from '../program-version.model';
import { ProgramVersionService } from '../service/program-version.service';

const programVersionResolve = (route: ActivatedRouteSnapshot): Observable<null | IProgramVersion> => {
  const id = route.params.id;
  if (id) {
    return inject(ProgramVersionService)
      .find(id)
      .pipe(
        mergeMap((programVersion: HttpResponse<IProgramVersion>) => {
          if (programVersion.body) {
            return of(programVersion.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default programVersionResolve;
