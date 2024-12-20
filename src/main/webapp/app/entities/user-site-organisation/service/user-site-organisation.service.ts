import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserSiteOrganisation, NewUserSiteOrganisation } from '../user-site-organisation.model';

export type PartialUpdateUserSiteOrganisation = Partial<IUserSiteOrganisation> & Pick<IUserSiteOrganisation, 'id'>;

export type EntityResponseType = HttpResponse<IUserSiteOrganisation>;
export type EntityArrayResponseType = HttpResponse<IUserSiteOrganisation[]>;

@Injectable({ providedIn: 'root' })
export class UserSiteOrganisationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-site-organisations');

  create(userSiteOrganisation: NewUserSiteOrganisation): Observable<EntityResponseType> {
    return this.http.post<IUserSiteOrganisation>(this.resourceUrl, userSiteOrganisation, { observe: 'response' });
  }

  update(userSiteOrganisation: IUserSiteOrganisation): Observable<EntityResponseType> {
    return this.http.put<IUserSiteOrganisation>(
      `${this.resourceUrl}/${this.getUserSiteOrganisationIdentifier(userSiteOrganisation)}`,
      userSiteOrganisation,
      { observe: 'response' },
    );
  }

  partialUpdate(userSiteOrganisation: PartialUpdateUserSiteOrganisation): Observable<EntityResponseType> {
    return this.http.patch<IUserSiteOrganisation>(
      `${this.resourceUrl}/${this.getUserSiteOrganisationIdentifier(userSiteOrganisation)}`,
      userSiteOrganisation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserSiteOrganisation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserSiteOrganisation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserSiteOrganisationIdentifier(userSiteOrganisation: Pick<IUserSiteOrganisation, 'id'>): number {
    return userSiteOrganisation.id;
  }

  compareUserSiteOrganisation(o1: Pick<IUserSiteOrganisation, 'id'> | null, o2: Pick<IUserSiteOrganisation, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserSiteOrganisationIdentifier(o1) === this.getUserSiteOrganisationIdentifier(o2) : o1 === o2;
  }

  addUserSiteOrganisationToCollectionIfMissing<Type extends Pick<IUserSiteOrganisation, 'id'>>(
    userSiteOrganisationCollection: Type[],
    ...userSiteOrganisationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userSiteOrganisations: Type[] = userSiteOrganisationsToCheck.filter(isPresent);
    if (userSiteOrganisations.length > 0) {
      const userSiteOrganisationCollectionIdentifiers = userSiteOrganisationCollection.map(userSiteOrganisationItem =>
        this.getUserSiteOrganisationIdentifier(userSiteOrganisationItem),
      );
      const userSiteOrganisationsToAdd = userSiteOrganisations.filter(userSiteOrganisationItem => {
        const userSiteOrganisationIdentifier = this.getUserSiteOrganisationIdentifier(userSiteOrganisationItem);
        if (userSiteOrganisationCollectionIdentifiers.includes(userSiteOrganisationIdentifier)) {
          return false;
        }
        userSiteOrganisationCollectionIdentifiers.push(userSiteOrganisationIdentifier);
        return true;
      });
      return [...userSiteOrganisationsToAdd, ...userSiteOrganisationCollection];
    }
    return userSiteOrganisationCollection;
  }
}
