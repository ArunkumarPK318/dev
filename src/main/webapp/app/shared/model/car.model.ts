import { Moment } from 'moment';
import { IEmployee } from 'app/shared/model/employee.model';

export interface ICar {
  id?: number;
  carName?: string;
  carModal?: string;
  brandName?: string;
  enginType?: string;
  engineNo?: number;
  manufactureDate?: Moment;
  employee?: IEmployee;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public carName?: string,
    public carModal?: string,
    public brandName?: string,
    public enginType?: string,
    public engineNo?: number,
    public manufactureDate?: Moment,
    public employee?: IEmployee
  ) {}
}
