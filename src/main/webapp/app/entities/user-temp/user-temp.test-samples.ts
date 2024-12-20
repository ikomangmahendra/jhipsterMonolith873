import { IUserTemp, NewUserTemp } from './user-temp.model';

export const sampleWithRequiredData: IUserTemp = {
  id: 32263,
};

export const sampleWithPartialData: IUserTemp = {
  id: 2959,
};

export const sampleWithFullData: IUserTemp = {
  id: 20775,
};

export const sampleWithNewData: NewUserTemp = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
