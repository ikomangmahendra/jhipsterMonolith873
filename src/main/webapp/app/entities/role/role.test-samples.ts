import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: 21267,
};

export const sampleWithPartialData: IRole = {
  id: 21331,
};

export const sampleWithFullData: IRole = {
  id: 31019,
};

export const sampleWithNewData: NewRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
