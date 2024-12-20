import { ISiteOrganisation, NewSiteOrganisation } from './site-organisation.model';

export const sampleWithRequiredData: ISiteOrganisation = {
  id: 17987,
};

export const sampleWithPartialData: ISiteOrganisation = {
  id: 58,
};

export const sampleWithFullData: ISiteOrganisation = {
  id: 30974,
};

export const sampleWithNewData: NewSiteOrganisation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
