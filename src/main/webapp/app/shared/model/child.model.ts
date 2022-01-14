import dayjs from 'dayjs';
import { IAddress } from 'app/shared/model/address.model';
import { IDescription } from 'app/shared/model/description.model';
import { IContactInformation } from 'app/shared/model/contact-information.model';
import { IUser } from 'app/shared/model/user.model';
import { ILastSeen } from 'app/shared/model/last-seen.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IChild {
  id?: number;
  imageContentType?: string | null;
  image?: string | null;
  name?: string;
  age?: number;
  gender?: Gender;
  birthdate?: string;
  status?: Status;
  agency?: string | null;
  address?: IAddress | null;
  description?: IDescription | null;
  contactInformation?: IContactInformation | null;
  user?: IUser | null;
  lastSeens?: ILastSeen[] | null;
}

export const defaultValue: Readonly<IChild> = {};
