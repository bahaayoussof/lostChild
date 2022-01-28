import { IChild } from 'app/shared/model/child.model';

export interface IContactInformation {
  id?: number;
  name?: string;
  email?: string;
  phoneNumber?: string;
  child?: IChild | null;
}

export const defaultValue: Readonly<IContactInformation> = {};
