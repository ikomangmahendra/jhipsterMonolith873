import dayjs from 'dayjs/esm';
import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { ProgramType } from 'app/entities/enumerations/program-type.model';
import { ProgramStatus } from 'app/entities/enumerations/program-status.model';

export interface IProgram {
  id: number;
  name?: string | null;
  type?: keyof typeof ProgramType | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof ProgramStatus | null;
  externalSystemLkp?: number | null;
  isEnableFollowUp?: boolean | null;
  isNsfSurveyAccess?: boolean | null;
  isOptOutAllowed?: boolean | null;
  coordinators?: Pick<IUserTemp, 'id'>[] | null;
}

export type NewProgram = Omit<IProgram, 'id'> & { id: null };
