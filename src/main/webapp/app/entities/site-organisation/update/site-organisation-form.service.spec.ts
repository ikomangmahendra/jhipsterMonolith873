import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../site-organisation.test-samples';

import { SiteOrganisationFormService } from './site-organisation-form.service';

describe('SiteOrganisation Form Service', () => {
  let service: SiteOrganisationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SiteOrganisationFormService);
  });

  describe('Service methods', () => {
    describe('createSiteOrganisationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSiteOrganisationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });

      it('passing ISiteOrganisation should create a new form with FormGroup', () => {
        const formGroup = service.createSiteOrganisationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });
    });

    describe('getSiteOrganisation', () => {
      it('should return NewSiteOrganisation for default SiteOrganisation initial value', () => {
        const formGroup = service.createSiteOrganisationFormGroup(sampleWithNewData);

        const siteOrganisation = service.getSiteOrganisation(formGroup) as any;

        expect(siteOrganisation).toMatchObject(sampleWithNewData);
      });

      it('should return NewSiteOrganisation for empty SiteOrganisation initial value', () => {
        const formGroup = service.createSiteOrganisationFormGroup();

        const siteOrganisation = service.getSiteOrganisation(formGroup) as any;

        expect(siteOrganisation).toMatchObject({});
      });

      it('should return ISiteOrganisation', () => {
        const formGroup = service.createSiteOrganisationFormGroup(sampleWithRequiredData);

        const siteOrganisation = service.getSiteOrganisation(formGroup) as any;

        expect(siteOrganisation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISiteOrganisation should not enable id FormControl', () => {
        const formGroup = service.createSiteOrganisationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSiteOrganisation should disable id FormControl', () => {
        const formGroup = service.createSiteOrganisationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
