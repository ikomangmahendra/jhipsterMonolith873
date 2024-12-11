import { IUserSiteOrganisation, NewUserSiteOrganisation } from './user-site-organisation.model';

export const sampleWithRequiredData: IUserSiteOrganisation = {
  id: 8651,
};

export const sampleWithPartialData: IUserSiteOrganisation = {
  id: 5075,
};

export const sampleWithFullData: IUserSiteOrganisation = {
  id: 27410,
};

export const sampleWithNewData: NewUserSiteOrganisation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
