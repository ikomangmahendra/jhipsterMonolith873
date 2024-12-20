import { IRole } from 'app/entities/role/role.model';
import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { ISiteOrganisation } from 'app/entities/site-organisation/site-organisation.model';

export interface IUserSiteOrganisation {
  id: number;
  roles?: IRole[] | null;
  user?: IUserTemp | null;
  siteOrganisation?: ISiteOrganisation | null;
}

export type NewUserSiteOrganisation = Omit<IUserSiteOrganisation, 'id'> & { id: null };
