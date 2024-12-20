import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: 9669,
};

export const sampleWithPartialData: IRole = {
  id: 8172,
};

export const sampleWithFullData: IRole = {
  id: 32266,
};

export const sampleWithNewData: NewRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
