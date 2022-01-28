import { ILastSeen } from 'app/shared/model/last-seen.model';

export interface ILastSeenAddress {
  id?: number;
  street?: string;
  city?: string;
  state?: string | null;
  country?: string | null;
  lastSeen?: ILastSeen | null;
}

export const defaultValue: Readonly<ILastSeenAddress> = {};
