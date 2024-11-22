import { IAdditionalVariable, NewAdditionalVariable } from './additional-variable.model';

export const sampleWithRequiredData: IAdditionalVariable = {
  id: 26037,
  programVersionId: 3930,
  jsonSchema: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IAdditionalVariable = {
  id: 23782,
  programVersionId: 14580,
  jsonSchema: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IAdditionalVariable = {
  id: 24770,
  programVersionId: 1352,
  jsonSchema: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAdditionalVariable = {
  programVersionId: 30759,
  jsonSchema: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
