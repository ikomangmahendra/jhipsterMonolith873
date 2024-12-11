export interface IRole {
  id: number;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
