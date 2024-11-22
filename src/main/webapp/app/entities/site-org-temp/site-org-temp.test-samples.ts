import { ISiteOrgTemp, NewSiteOrgTemp } from './site-org-temp.model';

export const sampleWithRequiredData: ISiteOrgTemp = {
  id: 5905,
};

export const sampleWithPartialData: ISiteOrgTemp = {
  id: 8109,
};

export const sampleWithFullData: ISiteOrgTemp = {
  id: 21648,
};

export const sampleWithNewData: NewSiteOrgTemp = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
