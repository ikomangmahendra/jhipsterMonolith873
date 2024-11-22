import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../accute-program-variable.test-samples';

import { AccuteProgramVariableFormService } from './accute-program-variable-form.service';

describe('AccuteProgramVariable Form Service', () => {
  let service: AccuteProgramVariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccuteProgramVariableFormService);
  });

  describe('Service methods', () => {
    describe('createAccuteProgramVariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccuteProgramVariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sectionId: expect.any(Object),
            sectionName: expect.any(Object),
            orderIndex: expect.any(Object),
            jsonSchema: expect.any(Object),
            version: expect.any(Object),
          }),
        );
      });

      it('passing IAccuteProgramVariable should create a new form with FormGroup', () => {
        const formGroup = service.createAccuteProgramVariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sectionId: expect.any(Object),
            sectionName: expect.any(Object),
            orderIndex: expect.any(Object),
            jsonSchema: expect.any(Object),
            version: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccuteProgramVariable', () => {
      it('should return NewAccuteProgramVariable for default AccuteProgramVariable initial value', () => {
        const formGroup = service.createAccuteProgramVariableFormGroup(sampleWithNewData);

        const accuteProgramVariable = service.getAccuteProgramVariable(formGroup) as any;

        expect(accuteProgramVariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccuteProgramVariable for empty AccuteProgramVariable initial value', () => {
        const formGroup = service.createAccuteProgramVariableFormGroup();

        const accuteProgramVariable = service.getAccuteProgramVariable(formGroup) as any;

        expect(accuteProgramVariable).toMatchObject({});
      });

      it('should return IAccuteProgramVariable', () => {
        const formGroup = service.createAccuteProgramVariableFormGroup(sampleWithRequiredData);

        const accuteProgramVariable = service.getAccuteProgramVariable(formGroup) as any;

        expect(accuteProgramVariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccuteProgramVariable should not enable id FormControl', () => {
        const formGroup = service.createAccuteProgramVariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccuteProgramVariable should disable id FormControl', () => {
        const formGroup = service.createAccuteProgramVariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
