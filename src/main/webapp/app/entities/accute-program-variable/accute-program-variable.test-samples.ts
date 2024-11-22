import { IAccuteProgramVariable, NewAccuteProgramVariable } from './accute-program-variable.model';

export const sampleWithRequiredData: IAccuteProgramVariable = {
  id: 10224,
  sectionId: 'ouch how for',
  sectionName: 'gerbil',
  orderIndex: 13347,
  jsonSchema: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IAccuteProgramVariable = {
  id: 17053,
  sectionId: 'past huzzah excluding',
  sectionName: 'dead including subdued',
  orderIndex: 29346,
  jsonSchema: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IAccuteProgramVariable = {
  id: 25463,
  sectionId: 'thoughtfully duh but',
  sectionName: 'offend',
  orderIndex: 28755,
  jsonSchema: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAccuteProgramVariable = {
  sectionId: 'drat secrecy',
  sectionName: 'though',
  orderIndex: 3195,
  jsonSchema: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
