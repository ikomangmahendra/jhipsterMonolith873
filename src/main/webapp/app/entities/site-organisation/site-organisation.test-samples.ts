import { ISiteOrganisation, NewSiteOrganisation } from './site-organisation.model';

export const sampleWithRequiredData: ISiteOrganisation = {
  id: 24639,
};

export const sampleWithPartialData: ISiteOrganisation = {
  id: 21673,
};

export const sampleWithFullData: ISiteOrganisation = {
  id: 31873,
};

export const sampleWithNewData: NewSiteOrganisation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
