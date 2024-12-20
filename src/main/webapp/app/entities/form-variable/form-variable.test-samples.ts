import { IFormVariable, NewFormVariable } from './form-variable.model';

export const sampleWithRequiredData: IFormVariable = {
  id: 29898,
  sectionCode: 'design bef',
  sectionName: 'down',
  formVariableType: 'NSF_SURVEY',
  orderIndex: 12759,
};

export const sampleWithPartialData: IFormVariable = {
  id: 29285,
  sectionCode: 'short-term',
  sectionName: 'dislocate upbeat',
  formVariableType: 'OUTCOME_ASSESSMENT',
  orderIndex: 3575,
};

export const sampleWithFullData: IFormVariable = {
  id: 11737,
  sectionCode: 'scaly woot',
  sectionName: 'hungrily quizzically',
  formVariableType: 'NSF_SURVEY',
  orderIndex: 32080,
};

export const sampleWithNewData: NewFormVariable = {
  sectionCode: 'cap',
  sectionName: 'who lack birdcage',
  formVariableType: 'ACUTE',
  orderIndex: 25599,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
