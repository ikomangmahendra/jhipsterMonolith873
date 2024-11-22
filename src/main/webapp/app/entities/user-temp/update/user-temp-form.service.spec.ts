import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-temp.test-samples';

import { UserTempFormService } from './user-temp-form.service';

describe('UserTemp Form Service', () => {
  let service: UserTempFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserTempFormService);
  });

  describe('Service methods', () => {
    describe('createUserTempFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserTempFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            programs: expect.any(Object),
          }),
        );
      });

      it('passing IUserTemp should create a new form with FormGroup', () => {
        const formGroup = service.createUserTempFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            programs: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserTemp', () => {
      it('should return NewUserTemp for default UserTemp initial value', () => {
        const formGroup = service.createUserTempFormGroup(sampleWithNewData);

        const userTemp = service.getUserTemp(formGroup) as any;

        expect(userTemp).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserTemp for empty UserTemp initial value', () => {
        const formGroup = service.createUserTempFormGroup();

        const userTemp = service.getUserTemp(formGroup) as any;

        expect(userTemp).toMatchObject({});
      });

      it('should return IUserTemp', () => {
        const formGroup = service.createUserTempFormGroup(sampleWithRequiredData);

        const userTemp = service.getUserTemp(formGroup) as any;

        expect(userTemp).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserTemp should not enable id FormControl', () => {
        const formGroup = service.createUserTempFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserTemp should disable id FormControl', () => {
        const formGroup = service.createUserTempFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
