import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserTemp, NewUserTemp } from '../user-temp.model';

export type EntityResponseType = HttpResponse<IUserTemp>;
export type EntityArrayResponseType = HttpResponse<IUserTemp[]>;

@Injectable({ providedIn: 'root' })
export class UserTempService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-temps');

  create(userTemp: NewUserTemp): Observable<EntityResponseType> {
    return this.http.post<IUserTemp>(this.resourceUrl, userTemp, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserTemp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserTemp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserTempIdentifier(userTemp: Pick<IUserTemp, 'id'>): number {
    return userTemp.id;
  }

  compareUserTemp(o1: Pick<IUserTemp, 'id'> | null, o2: Pick<IUserTemp, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserTempIdentifier(o1) === this.getUserTempIdentifier(o2) : o1 === o2;
  }

  addUserTempToCollectionIfMissing<Type extends Pick<IUserTemp, 'id'>>(
    userTempCollection: Type[],
    ...userTempsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userTemps: Type[] = userTempsToCheck.filter(isPresent);
    if (userTemps.length > 0) {
      const userTempCollectionIdentifiers = userTempCollection.map(userTempItem => this.getUserTempIdentifier(userTempItem));
      const userTempsToAdd = userTemps.filter(userTempItem => {
        const userTempIdentifier = this.getUserTempIdentifier(userTempItem);
        if (userTempCollectionIdentifiers.includes(userTempIdentifier)) {
          return false;
        }
        userTempCollectionIdentifiers.push(userTempIdentifier);
        return true;
      });
      return [...userTempsToAdd, ...userTempCollection];
    }
    return userTempCollection;
  }
}
