import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISiteOrganisation, NewSiteOrganisation } from '../site-organisation.model';

export type EntityResponseType = HttpResponse<ISiteOrganisation>;
export type EntityArrayResponseType = HttpResponse<ISiteOrganisation[]>;

@Injectable({ providedIn: 'root' })
export class SiteOrganisationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/site-organisations');

  create(siteOrganisation: NewSiteOrganisation): Observable<EntityResponseType> {
    return this.http.post<ISiteOrganisation>(this.resourceUrl, siteOrganisation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISiteOrganisation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISiteOrganisation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSiteOrganisationIdentifier(siteOrganisation: Pick<ISiteOrganisation, 'id'>): number {
    return siteOrganisation.id;
  }

  compareSiteOrganisation(o1: Pick<ISiteOrganisation, 'id'> | null, o2: Pick<ISiteOrganisation, 'id'> | null): boolean {
    return o1 && o2 ? this.getSiteOrganisationIdentifier(o1) === this.getSiteOrganisationIdentifier(o2) : o1 === o2;
  }

  addSiteOrganisationToCollectionIfMissing<Type extends Pick<ISiteOrganisation, 'id'>>(
    siteOrganisationCollection: Type[],
    ...siteOrganisationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const siteOrganisations: Type[] = siteOrganisationsToCheck.filter(isPresent);
    if (siteOrganisations.length > 0) {
      const siteOrganisationCollectionIdentifiers = siteOrganisationCollection.map(siteOrganisationItem =>
        this.getSiteOrganisationIdentifier(siteOrganisationItem),
      );
      const siteOrganisationsToAdd = siteOrganisations.filter(siteOrganisationItem => {
        const siteOrganisationIdentifier = this.getSiteOrganisationIdentifier(siteOrganisationItem);
        if (siteOrganisationCollectionIdentifiers.includes(siteOrganisationIdentifier)) {
          return false;
        }
        siteOrganisationCollectionIdentifiers.push(siteOrganisationIdentifier);
        return true;
      });
      return [...siteOrganisationsToAdd, ...siteOrganisationCollection];
    }
    return siteOrganisationCollection;
  }
}
