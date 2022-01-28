import { IChild } from 'app/shared/model/child.model';

export interface IAddress {
  id?: number;
  street?: string;
  city?: string;
  state?: string | null;
  country?: string | null;
  child?: IChild | null;
}

export const defaultValue: Readonly<IAddress> = {};
