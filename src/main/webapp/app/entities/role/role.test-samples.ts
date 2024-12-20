import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: 24555,
};

export const sampleWithPartialData: IRole = {
  id: 2350,
};

export const sampleWithFullData: IRole = {
  id: 11166,
};

export const sampleWithNewData: NewRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
