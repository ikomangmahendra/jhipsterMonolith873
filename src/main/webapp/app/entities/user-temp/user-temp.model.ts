export interface IUserTemp {
  id: number;
}

export type NewUserTemp = Omit<IUserTemp, 'id'> & { id: null };
