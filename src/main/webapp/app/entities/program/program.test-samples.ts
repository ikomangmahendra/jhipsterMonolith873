import dayjs from 'dayjs/esm';

import { IProgram, NewProgram } from './program.model';

export const sampleWithRequiredData: IProgram = {
  id: 27087,
  name: 'sushi ah litter',
  type: 'PERMANENT',
  status: 'CLOSED',
  externalSystemLkp: 32115,
  isEnableFollowUp: true,
  isNsfSurveyAccess: true,
  isOptOutAllowed: false,
};

export const sampleWithPartialData: IProgram = {
  id: 20968,
  name: 'furthermore about',
  type: 'TIME_LIMITED',
  startDate: dayjs('2024-11-21T11:38'),
  endDate: dayjs('2024-11-21T07:16'),
  status: 'ACTIVE',
  externalSystemLkp: 296,
  isEnableFollowUp: false,
  isNsfSurveyAccess: true,
  isOptOutAllowed: false,
};

export const sampleWithFullData: IProgram = {
  id: 816,
  name: 'unused subexpression',
  type: 'TIME_LIMITED',
  startDate: dayjs('2024-11-21T09:23'),
  endDate: dayjs('2024-11-21T12:53'),
  status: 'CLOSED',
  externalSystemLkp: 16586,
  isEnableFollowUp: true,
  isNsfSurveyAccess: true,
  isOptOutAllowed: false,
};

export const sampleWithNewData: NewProgram = {
  name: 'amid obedient consequently',
  type: 'PERMANENT',
  status: 'PENDING',
  externalSystemLkp: 27391,
  isEnableFollowUp: false,
  isNsfSurveyAccess: true,
  isOptOutAllowed: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
