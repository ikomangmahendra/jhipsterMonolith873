import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { ISiteOrganisation } from 'app/entities/site-organisation/site-organisation.model';
import { IRole } from 'app/entities/role/role.model';

export interface IUserSiteOrganisation {
  id: number;
  user?: IUserTemp | null;
  siteOrganisation?: ISiteOrganisation | null;
  role?: IRole | null;
}

export type NewUserSiteOrganisation = Omit<IUserSiteOrganisation, 'id'> & { id: null };
