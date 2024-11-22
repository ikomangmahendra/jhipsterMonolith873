import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccuteProgramVariable } from '../accute-program-variable.model';
import { AccuteProgramVariableService } from '../service/accute-program-variable.service';

const accuteProgramVariableResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccuteProgramVariable> => {
  const id = route.params.id;
  if (id) {
    return inject(AccuteProgramVariableService)
      .find(id)
      .pipe(
        mergeMap((accuteProgramVariable: HttpResponse<IAccuteProgramVariable>) => {
          if (accuteProgramVariable.body) {
            return of(accuteProgramVariable.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default accuteProgramVariableResolve;
