import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumer, NewConsumer } from '../consumer.model';

export type PartialUpdateConsumer = Partial<IConsumer> & Pick<IConsumer, 'id'>;

type RestOf<T extends IConsumer | NewConsumer> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestConsumer = RestOf<IConsumer>;

export type NewRestConsumer = RestOf<NewConsumer>;

export type PartialUpdateRestConsumer = RestOf<PartialUpdateConsumer>;

export type EntityResponseType = HttpResponse<IConsumer>;
export type EntityArrayResponseType = HttpResponse<IConsumer[]>;

@Injectable({ providedIn: 'root' })
export class ConsumerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consumers');

  create(consumer: NewConsumer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumer);
    return this.http
      .post<RestConsumer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(consumer: IConsumer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumer);
    return this.http
      .put<RestConsumer>(`${this.resourceUrl}/${this.getConsumerIdentifier(consumer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(consumer: PartialUpdateConsumer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumer);
    return this.http
      .patch<RestConsumer>(`${this.resourceUrl}/${this.getConsumerIdentifier(consumer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConsumer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConsumer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConsumerIdentifier(consumer: Pick<IConsumer, 'id'>): number {
    return consumer.id;
  }

  compareConsumer(o1: Pick<IConsumer, 'id'> | null, o2: Pick<IConsumer, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsumerIdentifier(o1) === this.getConsumerIdentifier(o2) : o1 === o2;
  }

  addConsumerToCollectionIfMissing<Type extends Pick<IConsumer, 'id'>>(
    consumerCollection: Type[],
    ...consumersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consumers: Type[] = consumersToCheck.filter(isPresent);
    if (consumers.length > 0) {
      const consumerCollectionIdentifiers = consumerCollection.map(consumerItem => this.getConsumerIdentifier(consumerItem));
      const consumersToAdd = consumers.filter(consumerItem => {
        const consumerIdentifier = this.getConsumerIdentifier(consumerItem);
        if (consumerCollectionIdentifiers.includes(consumerIdentifier)) {
          return false;
        }
        consumerCollectionIdentifiers.push(consumerIdentifier);
        return true;
      });
      return [...consumersToAdd, ...consumerCollection];
    }
    return consumerCollection;
  }

  protected convertDateFromClient<T extends IConsumer | NewConsumer | PartialUpdateConsumer>(consumer: T): RestOf<T> {
    return {
      ...consumer,
      createdDate: consumer.createdDate?.toJSON() ?? null,
      lastModifiedDate: consumer.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restConsumer: RestConsumer): IConsumer {
    return {
      ...restConsumer,
      createdDate: restConsumer.createdDate ? dayjs(restConsumer.createdDate) : undefined,
      lastModifiedDate: restConsumer.lastModifiedDate ? dayjs(restConsumer.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConsumer>): HttpResponse<IConsumer> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConsumer[]>): HttpResponse<IConsumer[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
