import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../additional-variable.test-samples';

import { AdditionalVariableFormService } from './additional-variable-form.service';

describe('AdditionalVariable Form Service', () => {
  let service: AdditionalVariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdditionalVariableFormService);
  });

  describe('Service methods', () => {
    describe('createAdditionalVariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdditionalVariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            programVersionId: expect.any(Object),
            jsonSchema: expect.any(Object),
            version: expect.any(Object),
            site: expect.any(Object),
          }),
        );
      });

      it('passing IAdditionalVariable should create a new form with FormGroup', () => {
        const formGroup = service.createAdditionalVariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            programVersionId: expect.any(Object),
            jsonSchema: expect.any(Object),
            version: expect.any(Object),
            site: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdditionalVariable', () => {
      it('should return NewAdditionalVariable for default AdditionalVariable initial value', () => {
        const formGroup = service.createAdditionalVariableFormGroup(sampleWithNewData);

        const additionalVariable = service.getAdditionalVariable(formGroup) as any;

        expect(additionalVariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdditionalVariable for empty AdditionalVariable initial value', () => {
        const formGroup = service.createAdditionalVariableFormGroup();

        const additionalVariable = service.getAdditionalVariable(formGroup) as any;

        expect(additionalVariable).toMatchObject({});
      });

      it('should return IAdditionalVariable', () => {
        const formGroup = service.createAdditionalVariableFormGroup(sampleWithRequiredData);

        const additionalVariable = service.getAdditionalVariable(formGroup) as any;

        expect(additionalVariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdditionalVariable should not enable id FormControl', () => {
        const formGroup = service.createAdditionalVariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdditionalVariable should disable id FormControl', () => {
        const formGroup = service.createAdditionalVariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
