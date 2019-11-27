import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICar, Car } from 'app/shared/model/car.model';
import { CarService } from './car.service';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee/employee.service';

@Component({
  selector: 'jhi-car-update',
  templateUrl: './car-update.component.html'
})
export class CarUpdateComponent implements OnInit {
  isSaving: boolean;

  employees: IEmployee[];

  editForm = this.fb.group({
    id: [],
    carName: [],
    carModal: [],
    brandName: [],
    enginType: [],
    engineNo: [],
    manufactureDate: [],
    employee: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected carService: CarService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ car }) => {
      this.updateForm(car);
    });
    this.employeeService
      .query()
      .subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(car: ICar) {
    this.editForm.patchValue({
      id: car.id,
      carName: car.carName,
      carModal: car.carModal,
      brandName: car.brandName,
      enginType: car.enginType,
      engineNo: car.engineNo,
      manufactureDate: car.manufactureDate != null ? car.manufactureDate.format(DATE_TIME_FORMAT) : null,
      employee: car.employee
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const car = this.createFromForm();
    if (car.id !== undefined) {
      this.subscribeToSaveResponse(this.carService.update(car));
    } else {
      this.subscribeToSaveResponse(this.carService.create(car));
    }
  }

  private createFromForm(): ICar {
    return {
      ...new Car(),
      id: this.editForm.get(['id']).value,
      carName: this.editForm.get(['carName']).value,
      carModal: this.editForm.get(['carModal']).value,
      brandName: this.editForm.get(['brandName']).value,
      enginType: this.editForm.get(['enginType']).value,
      engineNo: this.editForm.get(['engineNo']).value,
      manufactureDate:
        this.editForm.get(['manufactureDate']).value != null
          ? moment(this.editForm.get(['manufactureDate']).value, DATE_TIME_FORMAT)
          : undefined,
      employee: this.editForm.get(['employee']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICar>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackEmployeeById(index: number, item: IEmployee) {
    return item.id;
  }
}
