import dayjs from 'dayjs/esm';

import { IProgramVersion, NewProgramVersion } from './program-version.model';

export const sampleWithRequiredData: IProgramVersion = {
  id: 16441,
  version: 528,
  isActive: false,
};

export const sampleWithPartialData: IProgramVersion = {
  id: 6887,
  version: 21079,
  isActive: false,
};

export const sampleWithFullData: IProgramVersion = {
  id: 22385,
  version: 5684,
  isActive: false,
  publishDate: dayjs('2024-11-21'),
};

export const sampleWithNewData: NewProgramVersion = {
  version: 6779,
  isActive: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
