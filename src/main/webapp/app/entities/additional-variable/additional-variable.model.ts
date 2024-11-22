import { IProgramVersion } from 'app/entities/program-version/program-version.model';
import { ISiteOrgTemp } from 'app/entities/site-org-temp/site-org-temp.model';

export interface IAdditionalVariable {
  id: number;
  programVersionId?: number | null;
  jsonSchema?: string | null;
  version?: Pick<IProgramVersion, 'id'> | null;
  site?: Pick<ISiteOrgTemp, 'id'> | null;
}

export type NewAdditionalVariable = Omit<IAdditionalVariable, 'id'> & { id: null };
