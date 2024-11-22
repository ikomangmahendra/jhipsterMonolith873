import dayjs from 'dayjs/esm';

import { IProgram, NewProgram } from './program.model';

export const sampleWithRequiredData: IProgram = {
  id: 16506,
  name: 'mmm or huzzah',
  type: 'PERMANENT',
  status: 'CLOSED',
  externalSystemLkp: 27723,
  isEnableFollowUp: false,
  isNsfSurveyAccess: false,
  isOptOutAllowed: false,
};

export const sampleWithPartialData: IProgram = {
  id: 28588,
  name: 'larva',
  type: 'PERMANENT',
  status: 'CLOSED',
  externalSystemLkp: 13045,
  isEnableFollowUp: true,
  isNsfSurveyAccess: true,
  isOptOutAllowed: false,
};

export const sampleWithFullData: IProgram = {
  id: 5999,
  name: 'tomatillo optimistically',
  type: 'TIME_LIMITED',
  startDate: dayjs('2024-11-21T16:49'),
  endDate: dayjs('2024-11-21T15:26'),
  status: 'PENDING',
  externalSystemLkp: 27533,
  isEnableFollowUp: false,
  isNsfSurveyAccess: false,
  isOptOutAllowed: true,
};

export const sampleWithNewData: NewProgram = {
  name: 'until boohoo',
  type: 'PERMANENT',
  status: 'PENDING',
  externalSystemLkp: 1484,
  isEnableFollowUp: false,
  isNsfSurveyAccess: false,
  isOptOutAllowed: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
