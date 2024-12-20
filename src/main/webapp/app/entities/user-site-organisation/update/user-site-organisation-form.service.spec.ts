import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-site-organisation.test-samples';

import { UserSiteOrganisationFormService } from './user-site-organisation-form.service';

describe('UserSiteOrganisation Form Service', () => {
  let service: UserSiteOrganisationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserSiteOrganisationFormService);
  });

  describe('Service methods', () => {
    describe('createUserSiteOrganisationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserSiteOrganisationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roles: expect.any(Object),
            user: expect.any(Object),
            siteOrganisation: expect.any(Object),
          }),
        );
      });

      it('passing IUserSiteOrganisation should create a new form with FormGroup', () => {
        const formGroup = service.createUserSiteOrganisationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roles: expect.any(Object),
            user: expect.any(Object),
            siteOrganisation: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserSiteOrganisation', () => {
      it('should return NewUserSiteOrganisation for default UserSiteOrganisation initial value', () => {
        const formGroup = service.createUserSiteOrganisationFormGroup(sampleWithNewData);

        const userSiteOrganisation = service.getUserSiteOrganisation(formGroup) as any;

        expect(userSiteOrganisation).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserSiteOrganisation for empty UserSiteOrganisation initial value', () => {
        const formGroup = service.createUserSiteOrganisationFormGroup();

        const userSiteOrganisation = service.getUserSiteOrganisation(formGroup) as any;

        expect(userSiteOrganisation).toMatchObject({});
      });

      it('should return IUserSiteOrganisation', () => {
        const formGroup = service.createUserSiteOrganisationFormGroup(sampleWithRequiredData);

        const userSiteOrganisation = service.getUserSiteOrganisation(formGroup) as any;

        expect(userSiteOrganisation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserSiteOrganisation should not enable id FormControl', () => {
        const formGroup = service.createUserSiteOrganisationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserSiteOrganisation should disable id FormControl', () => {
        const formGroup = service.createUserSiteOrganisationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
