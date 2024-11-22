import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdditionalVariable } from '../additional-variable.model';
import { AdditionalVariableService } from '../service/additional-variable.service';

const additionalVariableResolve = (route: ActivatedRouteSnapshot): Observable<null | IAdditionalVariable> => {
  const id = route.params.id;
  if (id) {
    return inject(AdditionalVariableService)
      .find(id)
      .pipe(
        mergeMap((additionalVariable: HttpResponse<IAdditionalVariable>) => {
          if (additionalVariable.body) {
            return of(additionalVariable.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default additionalVariableResolve;
