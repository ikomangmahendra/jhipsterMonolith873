import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccuteProgramVariable, NewAccuteProgramVariable } from '../accute-program-variable.model';

export type PartialUpdateAccuteProgramVariable = Partial<IAccuteProgramVariable> & Pick<IAccuteProgramVariable, 'id'>;

export type EntityResponseType = HttpResponse<IAccuteProgramVariable>;
export type EntityArrayResponseType = HttpResponse<IAccuteProgramVariable[]>;

@Injectable({ providedIn: 'root' })
export class AccuteProgramVariableService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accute-program-variables');

  create(accuteProgramVariable: NewAccuteProgramVariable): Observable<EntityResponseType> {
    return this.http.post<IAccuteProgramVariable>(this.resourceUrl, accuteProgramVariable, { observe: 'response' });
  }

  update(accuteProgramVariable: IAccuteProgramVariable): Observable<EntityResponseType> {
    return this.http.put<IAccuteProgramVariable>(
      `${this.resourceUrl}/${this.getAccuteProgramVariableIdentifier(accuteProgramVariable)}`,
      accuteProgramVariable,
      { observe: 'response' },
    );
  }

  partialUpdate(accuteProgramVariable: PartialUpdateAccuteProgramVariable): Observable<EntityResponseType> {
    return this.http.patch<IAccuteProgramVariable>(
      `${this.resourceUrl}/${this.getAccuteProgramVariableIdentifier(accuteProgramVariable)}`,
      accuteProgramVariable,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccuteProgramVariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccuteProgramVariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccuteProgramVariableIdentifier(accuteProgramVariable: Pick<IAccuteProgramVariable, 'id'>): number {
    return accuteProgramVariable.id;
  }

  compareAccuteProgramVariable(o1: Pick<IAccuteProgramVariable, 'id'> | null, o2: Pick<IAccuteProgramVariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccuteProgramVariableIdentifier(o1) === this.getAccuteProgramVariableIdentifier(o2) : o1 === o2;
  }

  addAccuteProgramVariableToCollectionIfMissing<Type extends Pick<IAccuteProgramVariable, 'id'>>(
    accuteProgramVariableCollection: Type[],
    ...accuteProgramVariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accuteProgramVariables: Type[] = accuteProgramVariablesToCheck.filter(isPresent);
    if (accuteProgramVariables.length > 0) {
      const accuteProgramVariableCollectionIdentifiers = accuteProgramVariableCollection.map(accuteProgramVariableItem =>
        this.getAccuteProgramVariableIdentifier(accuteProgramVariableItem),
      );
      const accuteProgramVariablesToAdd = accuteProgramVariables.filter(accuteProgramVariableItem => {
        const accuteProgramVariableIdentifier = this.getAccuteProgramVariableIdentifier(accuteProgramVariableItem);
        if (accuteProgramVariableCollectionIdentifiers.includes(accuteProgramVariableIdentifier)) {
          return false;
        }
        accuteProgramVariableCollectionIdentifiers.push(accuteProgramVariableIdentifier);
        return true;
      });
      return [...accuteProgramVariablesToAdd, ...accuteProgramVariableCollection];
    }
    return accuteProgramVariableCollection;
  }
}
