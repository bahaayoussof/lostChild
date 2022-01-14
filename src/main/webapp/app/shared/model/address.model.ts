import { IChild } from 'app/shared/model/child.model';
import { ILastSeen } from 'app/shared/model/last-seen.model';

export interface IAddress {
  id?: number;
  street?: string;
  city?: string;
  state?: string | null;
  country?: string | null;
  child?: IChild | null;
  lastSeen?: ILastSeen | null;
}

export const defaultValue: Readonly<IAddress> = {};
