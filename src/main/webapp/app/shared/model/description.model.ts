import { IChild } from 'app/shared/model/child.model';

export interface IDescription {
  id?: number;
  eyeColor?: string;
  hairColor?: string;
  weight?: number;
  height?: number;
  child?: IChild | null;
}

export const defaultValue: Readonly<IDescription> = {};
