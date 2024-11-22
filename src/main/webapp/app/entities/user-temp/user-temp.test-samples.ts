import { IUserTemp, NewUserTemp } from './user-temp.model';

export const sampleWithRequiredData: IUserTemp = {
  id: 16667,
};

export const sampleWithPartialData: IUserTemp = {
  id: 31815,
};

export const sampleWithFullData: IUserTemp = {
  id: 22998,
};

export const sampleWithNewData: NewUserTemp = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
