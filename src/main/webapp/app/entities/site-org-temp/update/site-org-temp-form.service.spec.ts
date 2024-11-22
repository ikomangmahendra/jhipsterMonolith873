import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../site-org-temp.test-samples';

import { SiteOrgTempFormService } from './site-org-temp-form.service';

describe('SiteOrgTemp Form Service', () => {
  let service: SiteOrgTempFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SiteOrgTempFormService);
  });

  describe('Service methods', () => {
    describe('createSiteOrgTempFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSiteOrgTempFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });

      it('passing ISiteOrgTemp should create a new form with FormGroup', () => {
        const formGroup = service.createSiteOrgTempFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });
    });

    describe('getSiteOrgTemp', () => {
      it('should return NewSiteOrgTemp for default SiteOrgTemp initial value', () => {
        const formGroup = service.createSiteOrgTempFormGroup(sampleWithNewData);

        const siteOrgTemp = service.getSiteOrgTemp(formGroup) as any;

        expect(siteOrgTemp).toMatchObject(sampleWithNewData);
      });

      it('should return NewSiteOrgTemp for empty SiteOrgTemp initial value', () => {
        const formGroup = service.createSiteOrgTempFormGroup();

        const siteOrgTemp = service.getSiteOrgTemp(formGroup) as any;

        expect(siteOrgTemp).toMatchObject({});
      });

      it('should return ISiteOrgTemp', () => {
        const formGroup = service.createSiteOrgTempFormGroup(sampleWithRequiredData);

        const siteOrgTemp = service.getSiteOrgTemp(formGroup) as any;

        expect(siteOrgTemp).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISiteOrgTemp should not enable id FormControl', () => {
        const formGroup = service.createSiteOrgTempFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSiteOrgTemp should disable id FormControl', () => {
        const formGroup = service.createSiteOrgTempFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
