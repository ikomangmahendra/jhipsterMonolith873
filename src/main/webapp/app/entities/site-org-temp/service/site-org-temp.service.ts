import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISiteOrgTemp, NewSiteOrgTemp } from '../site-org-temp.model';

export type EntityResponseType = HttpResponse<ISiteOrgTemp>;
export type EntityArrayResponseType = HttpResponse<ISiteOrgTemp[]>;

@Injectable({ providedIn: 'root' })
export class SiteOrgTempService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/site-org-temps');

  create(siteOrgTemp: NewSiteOrgTemp): Observable<EntityResponseType> {
    return this.http.post<ISiteOrgTemp>(this.resourceUrl, siteOrgTemp, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISiteOrgTemp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISiteOrgTemp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSiteOrgTempIdentifier(siteOrgTemp: Pick<ISiteOrgTemp, 'id'>): number {
    return siteOrgTemp.id;
  }

  compareSiteOrgTemp(o1: Pick<ISiteOrgTemp, 'id'> | null, o2: Pick<ISiteOrgTemp, 'id'> | null): boolean {
    return o1 && o2 ? this.getSiteOrgTempIdentifier(o1) === this.getSiteOrgTempIdentifier(o2) : o1 === o2;
  }

  addSiteOrgTempToCollectionIfMissing<Type extends Pick<ISiteOrgTemp, 'id'>>(
    siteOrgTempCollection: Type[],
    ...siteOrgTempsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const siteOrgTemps: Type[] = siteOrgTempsToCheck.filter(isPresent);
    if (siteOrgTemps.length > 0) {
      const siteOrgTempCollectionIdentifiers = siteOrgTempCollection.map(siteOrgTempItem => this.getSiteOrgTempIdentifier(siteOrgTempItem));
      const siteOrgTempsToAdd = siteOrgTemps.filter(siteOrgTempItem => {
        const siteOrgTempIdentifier = this.getSiteOrgTempIdentifier(siteOrgTempItem);
        if (siteOrgTempCollectionIdentifiers.includes(siteOrgTempIdentifier)) {
          return false;
        }
        siteOrgTempCollectionIdentifiers.push(siteOrgTempIdentifier);
        return true;
      });
      return [...siteOrgTempsToAdd, ...siteOrgTempCollection];
    }
    return siteOrgTempCollection;
  }
}
