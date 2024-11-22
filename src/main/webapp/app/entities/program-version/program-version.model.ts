import dayjs from 'dayjs/esm';
import { IProgram } from 'app/entities/program/program.model';

export interface IProgramVersion {
  id: number;
  version?: number | null;
  isActive?: boolean | null;
  publishDate?: dayjs.Dayjs | null;
  program?: Pick<IProgram, 'id'> | null;
}

export type NewProgramVersion = Omit<IProgramVersion, 'id'> & { id: null };
