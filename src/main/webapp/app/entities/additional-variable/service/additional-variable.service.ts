import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdditionalVariable, NewAdditionalVariable } from '../additional-variable.model';

export type PartialUpdateAdditionalVariable = Partial<IAdditionalVariable> & Pick<IAdditionalVariable, 'id'>;

export type EntityResponseType = HttpResponse<IAdditionalVariable>;
export type EntityArrayResponseType = HttpResponse<IAdditionalVariable[]>;

@Injectable({ providedIn: 'root' })
export class AdditionalVariableService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/additional-variables');

  create(additionalVariable: NewAdditionalVariable): Observable<EntityResponseType> {
    return this.http.post<IAdditionalVariable>(this.resourceUrl, additionalVariable, { observe: 'response' });
  }

  update(additionalVariable: IAdditionalVariable): Observable<EntityResponseType> {
    return this.http.put<IAdditionalVariable>(
      `${this.resourceUrl}/${this.getAdditionalVariableIdentifier(additionalVariable)}`,
      additionalVariable,
      { observe: 'response' },
    );
  }

  partialUpdate(additionalVariable: PartialUpdateAdditionalVariable): Observable<EntityResponseType> {
    return this.http.patch<IAdditionalVariable>(
      `${this.resourceUrl}/${this.getAdditionalVariableIdentifier(additionalVariable)}`,
      additionalVariable,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdditionalVariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdditionalVariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdditionalVariableIdentifier(additionalVariable: Pick<IAdditionalVariable, 'id'>): number {
    return additionalVariable.id;
  }

  compareAdditionalVariable(o1: Pick<IAdditionalVariable, 'id'> | null, o2: Pick<IAdditionalVariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdditionalVariableIdentifier(o1) === this.getAdditionalVariableIdentifier(o2) : o1 === o2;
  }

  addAdditionalVariableToCollectionIfMissing<Type extends Pick<IAdditionalVariable, 'id'>>(
    additionalVariableCollection: Type[],
    ...additionalVariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const additionalVariables: Type[] = additionalVariablesToCheck.filter(isPresent);
    if (additionalVariables.length > 0) {
      const additionalVariableCollectionIdentifiers = additionalVariableCollection.map(additionalVariableItem =>
        this.getAdditionalVariableIdentifier(additionalVariableItem),
      );
      const additionalVariablesToAdd = additionalVariables.filter(additionalVariableItem => {
        const additionalVariableIdentifier = this.getAdditionalVariableIdentifier(additionalVariableItem);
        if (additionalVariableCollectionIdentifiers.includes(additionalVariableIdentifier)) {
          return false;
        }
        additionalVariableCollectionIdentifiers.push(additionalVariableIdentifier);
        return true;
      });
      return [...additionalVariablesToAdd, ...additionalVariableCollection];
    }
    return additionalVariableCollection;
  }
}
