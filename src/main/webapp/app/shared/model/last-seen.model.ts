import dayjs from 'dayjs';
import { IAddress } from 'app/shared/model/address.model';
import { IChild } from 'app/shared/model/child.model';

export interface ILastSeen {
  id?: number;
  date?: string;
  address?: IAddress | null;
  child?: IChild | null;
}

export const defaultValue: Readonly<ILastSeen> = {};
