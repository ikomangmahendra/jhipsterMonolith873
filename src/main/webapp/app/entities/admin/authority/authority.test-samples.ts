import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '80359e71-b327-4077-9af2-3367fd9cfbae',
};

export const sampleWithPartialData: IAuthority = {
  name: 'f9d84572-bf3f-43a4-86ad-805bbe174b27',
};

export const sampleWithFullData: IAuthority = {
  name: '96ad1f25-eb38-47cc-ab8c-82bef447f0d3',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
