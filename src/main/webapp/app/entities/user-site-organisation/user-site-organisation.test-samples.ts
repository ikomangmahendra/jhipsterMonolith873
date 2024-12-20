import { IUserSiteOrganisation, NewUserSiteOrganisation } from './user-site-organisation.model';

export const sampleWithRequiredData: IUserSiteOrganisation = {
  id: 29707,
};

export const sampleWithPartialData: IUserSiteOrganisation = {
  id: 10624,
};

export const sampleWithFullData: IUserSiteOrganisation = {
  id: 22633,
};

export const sampleWithNewData: NewUserSiteOrganisation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
