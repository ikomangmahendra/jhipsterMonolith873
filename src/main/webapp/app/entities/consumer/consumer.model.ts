import dayjs from 'dayjs/esm';

export interface IConsumer {
  id: number;
  guid?: string | null;
  note?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  recordStatusId?: number | null;
}

export type NewConsumer = Omit<IConsumer, 'id'> & { id: null };
