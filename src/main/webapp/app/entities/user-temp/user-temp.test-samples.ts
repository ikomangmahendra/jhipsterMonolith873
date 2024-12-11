import { IUserTemp, NewUserTemp } from './user-temp.model';

export const sampleWithRequiredData: IUserTemp = {
  id: 8740,
};

export const sampleWithPartialData: IUserTemp = {
  id: 31164,
};

export const sampleWithFullData: IUserTemp = {
  id: 24775,
};

export const sampleWithNewData: NewUserTemp = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
