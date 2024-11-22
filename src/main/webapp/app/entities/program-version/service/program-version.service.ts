import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProgramVersion, NewProgramVersion } from '../program-version.model';

export type PartialUpdateProgramVersion = Partial<IProgramVersion> & Pick<IProgramVersion, 'id'>;

type RestOf<T extends IProgramVersion | NewProgramVersion> = Omit<T, 'publishDate'> & {
  publishDate?: string | null;
};

export type RestProgramVersion = RestOf<IProgramVersion>;

export type NewRestProgramVersion = RestOf<NewProgramVersion>;

export type PartialUpdateRestProgramVersion = RestOf<PartialUpdateProgramVersion>;

export type EntityResponseType = HttpResponse<IProgramVersion>;
export type EntityArrayResponseType = HttpResponse<IProgramVersion[]>;

@Injectable({ providedIn: 'root' })
export class ProgramVersionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/program-versions');

  create(programVersion: NewProgramVersion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programVersion);
    return this.http
      .post<RestProgramVersion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(programVersion: IProgramVersion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programVersion);
    return this.http
      .put<RestProgramVersion>(`${this.resourceUrl}/${this.getProgramVersionIdentifier(programVersion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(programVersion: PartialUpdateProgramVersion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programVersion);
    return this.http
      .patch<RestProgramVersion>(`${this.resourceUrl}/${this.getProgramVersionIdentifier(programVersion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProgramVersion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProgramVersion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProgramVersionIdentifier(programVersion: Pick<IProgramVersion, 'id'>): number {
    return programVersion.id;
  }

  compareProgramVersion(o1: Pick<IProgramVersion, 'id'> | null, o2: Pick<IProgramVersion, 'id'> | null): boolean {
    return o1 && o2 ? this.getProgramVersionIdentifier(o1) === this.getProgramVersionIdentifier(o2) : o1 === o2;
  }

  addProgramVersionToCollectionIfMissing<Type extends Pick<IProgramVersion, 'id'>>(
    programVersionCollection: Type[],
    ...programVersionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const programVersions: Type[] = programVersionsToCheck.filter(isPresent);
    if (programVersions.length > 0) {
      const programVersionCollectionIdentifiers = programVersionCollection.map(programVersionItem =>
        this.getProgramVersionIdentifier(programVersionItem),
      );
      const programVersionsToAdd = programVersions.filter(programVersionItem => {
        const programVersionIdentifier = this.getProgramVersionIdentifier(programVersionItem);
        if (programVersionCollectionIdentifiers.includes(programVersionIdentifier)) {
          return false;
        }
        programVersionCollectionIdentifiers.push(programVersionIdentifier);
        return true;
      });
      return [...programVersionsToAdd, ...programVersionCollection];
    }
    return programVersionCollection;
  }

  protected convertDateFromClient<T extends IProgramVersion | NewProgramVersion | PartialUpdateProgramVersion>(
    programVersion: T,
  ): RestOf<T> {
    return {
      ...programVersion,
      publishDate: programVersion.publishDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProgramVersion: RestProgramVersion): IProgramVersion {
    return {
      ...restProgramVersion,
      publishDate: restProgramVersion.publishDate ? dayjs(restProgramVersion.publishDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProgramVersion>): HttpResponse<IProgramVersion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProgramVersion[]>): HttpResponse<IProgramVersion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
