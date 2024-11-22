import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../program-version.test-samples';

import { ProgramVersionFormService } from './program-version-form.service';

describe('ProgramVersion Form Service', () => {
  let service: ProgramVersionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProgramVersionFormService);
  });

  describe('Service methods', () => {
    describe('createProgramVersionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProgramVersionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            version: expect.any(Object),
            isActive: expect.any(Object),
            publishDate: expect.any(Object),
            program: expect.any(Object),
          }),
        );
      });

      it('passing IProgramVersion should create a new form with FormGroup', () => {
        const formGroup = service.createProgramVersionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            version: expect.any(Object),
            isActive: expect.any(Object),
            publishDate: expect.any(Object),
            program: expect.any(Object),
          }),
        );
      });
    });

    describe('getProgramVersion', () => {
      it('should return NewProgramVersion for default ProgramVersion initial value', () => {
        const formGroup = service.createProgramVersionFormGroup(sampleWithNewData);

        const programVersion = service.getProgramVersion(formGroup) as any;

        expect(programVersion).toMatchObject(sampleWithNewData);
      });

      it('should return NewProgramVersion for empty ProgramVersion initial value', () => {
        const formGroup = service.createProgramVersionFormGroup();

        const programVersion = service.getProgramVersion(formGroup) as any;

        expect(programVersion).toMatchObject({});
      });

      it('should return IProgramVersion', () => {
        const formGroup = service.createProgramVersionFormGroup(sampleWithRequiredData);

        const programVersion = service.getProgramVersion(formGroup) as any;

        expect(programVersion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProgramVersion should not enable id FormControl', () => {
        const formGroup = service.createProgramVersionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProgramVersion should disable id FormControl', () => {
        const formGroup = service.createProgramVersionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
