import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 22919,
  login: "_@yn\\}T\\'w5U-",
};

export const sampleWithPartialData: IUser = {
  id: 19800,
  login: '9839C',
};

export const sampleWithFullData: IUser = {
  id: 10183,
  login: '-hA5@xR49b\\Wl3CY7o',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
