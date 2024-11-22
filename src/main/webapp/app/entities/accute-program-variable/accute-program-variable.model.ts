import { IProgramVersion } from 'app/entities/program-version/program-version.model';

export interface IAccuteProgramVariable {
  id: number;
  sectionId?: string | null;
  sectionName?: string | null;
  orderIndex?: number | null;
  jsonSchema?: string | null;
  version?: Pick<IProgramVersion, 'id'> | null;
}

export type NewAccuteProgramVariable = Omit<IAccuteProgramVariable, 'id'> & { id: null };
