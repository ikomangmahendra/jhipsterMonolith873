import { IProgram } from 'app/entities/program/program.model';

export interface IUserTemp {
  id: number;
  programs?: IProgram[] | null;
}

export type NewUserTemp = Omit<IUserTemp, 'id'> & { id: null };
