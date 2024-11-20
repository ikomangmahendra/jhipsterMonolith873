import dayjs from 'dayjs/esm';

import { IConsumer, NewConsumer } from './consumer.model';

export const sampleWithRequiredData: IConsumer = {
  id: 13753,
  guid: 'brook',
  recordStatusId: 23795,
};

export const sampleWithPartialData: IConsumer = {
  id: 18655,
  guid: 'jaywalk down',
  note: 'noisily normal now',
  createdBy: 'now monstrous',
  recordStatusId: 25752,
};

export const sampleWithFullData: IConsumer = {
  id: 30932,
  guid: 'brr unless progress',
  note: 'famously',
  createdBy: 'scram woot bump',
  createdDate: dayjs('2024-11-19T18:00'),
  lastModifiedBy: 'shovel',
  lastModifiedDate: dayjs('2024-11-19T21:31'),
  recordStatusId: 31647,
};

export const sampleWithNewData: NewConsumer = {
  guid: 'but harp',
  recordStatusId: 15129,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
