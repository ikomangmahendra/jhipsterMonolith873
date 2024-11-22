import { IProgram } from 'app/entities/program/program.model';

export interface IUserTemp {
  id: number;
  programs?: Pick<IProgram, 'id'>[] | null;
}

export type NewUserTemp = Omit<IUserTemp, 'id'> & { id: null };
