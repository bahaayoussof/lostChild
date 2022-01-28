import dayjs from 'dayjs';
import { ILastSeenAddress } from 'app/shared/model/last-seen-address.model';
import { IChild } from 'app/shared/model/child.model';

export interface ILastSeen {
  id?: number;
  date?: string;
  lastSeenAddress?: ILastSeenAddress | null;
  child?: IChild | null;
}

export const defaultValue: Readonly<ILastSeen> = {};
