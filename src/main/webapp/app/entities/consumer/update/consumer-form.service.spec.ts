import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../consumer.test-samples';

import { ConsumerFormService } from './consumer-form.service';

describe('Consumer Form Service', () => {
  let service: ConsumerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsumerFormService);
  });

  describe('Service methods', () => {
    describe('createConsumerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConsumerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            guid: expect.any(Object),
            note: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            recordStatusId: expect.any(Object),
          }),
        );
      });

      it('passing IConsumer should create a new form with FormGroup', () => {
        const formGroup = service.createConsumerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            guid: expect.any(Object),
            note: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            recordStatusId: expect.any(Object),
          }),
        );
      });
    });

    describe('getConsumer', () => {
      it('should return NewConsumer for default Consumer initial value', () => {
        const formGroup = service.createConsumerFormGroup(sampleWithNewData);

        const consumer = service.getConsumer(formGroup) as any;

        expect(consumer).toMatchObject(sampleWithNewData);
      });

      it('should return NewConsumer for empty Consumer initial value', () => {
        const formGroup = service.createConsumerFormGroup();

        const consumer = service.getConsumer(formGroup) as any;

        expect(consumer).toMatchObject({});
      });

      it('should return IConsumer', () => {
        const formGroup = service.createConsumerFormGroup(sampleWithRequiredData);

        const consumer = service.getConsumer(formGroup) as any;

        expect(consumer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConsumer should not enable id FormControl', () => {
        const formGroup = service.createConsumerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConsumer should disable id FormControl', () => {
        const formGroup = service.createConsumerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
