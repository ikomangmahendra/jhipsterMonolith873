import { IUserTemp, NewUserTemp } from './user-temp.model';

export const sampleWithRequiredData: IUserTemp = {
  id: 4248,
};

export const sampleWithPartialData: IUserTemp = {
  id: 17602,
};

export const sampleWithFullData: IUserTemp = {
  id: 29878,
};

export const sampleWithNewData: NewUserTemp = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
