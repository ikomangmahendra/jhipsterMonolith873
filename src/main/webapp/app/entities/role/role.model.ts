import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { IUserSiteOrganisation } from 'app/entities/user-site-organisation/user-site-organisation.model';

export interface IRole {
  id: number;
  user?: IUserTemp | null;
  userSiteOrganisation?: IUserSiteOrganisation | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
