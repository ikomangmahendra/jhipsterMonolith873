import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUserSiteOrganisation, NewUserSiteOrganisation } from '../user-site-organisation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserSiteOrganisation for edit and NewUserSiteOrganisationFormGroupInput for create.
 */
type UserSiteOrganisationFormGroupInput = IUserSiteOrganisation | PartialWithRequiredKeyOf<NewUserSiteOrganisation>;

type UserSiteOrganisationFormDefaults = Pick<NewUserSiteOrganisation, 'id' | 'roles'>;

type UserSiteOrganisationFormGroupContent = {
  id: FormControl<IUserSiteOrganisation['id'] | NewUserSiteOrganisation['id']>;
  roles: FormControl<IUserSiteOrganisation['roles']>;
  user: FormControl<IUserSiteOrganisation['user']>;
  siteOrganisation: FormControl<IUserSiteOrganisation['siteOrganisation']>;
};

export type UserSiteOrganisationFormGroup = FormGroup<UserSiteOrganisationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserSiteOrganisationFormService {
  createUserSiteOrganisationFormGroup(
    userSiteOrganisation: UserSiteOrganisationFormGroupInput = { id: null },
  ): UserSiteOrganisationFormGroup {
    const userSiteOrganisationRawValue = {
      ...this.getFormDefaults(),
      ...userSiteOrganisation,
    };
    return new FormGroup<UserSiteOrganisationFormGroupContent>({
      id: new FormControl(
        { value: userSiteOrganisationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      roles: new FormControl(userSiteOrganisationRawValue.roles ?? []),
      user: new FormControl(userSiteOrganisationRawValue.user),
      siteOrganisation: new FormControl(userSiteOrganisationRawValue.siteOrganisation),
    });
  }

  getUserSiteOrganisation(form: UserSiteOrganisationFormGroup): IUserSiteOrganisation | NewUserSiteOrganisation {
    return form.getRawValue() as IUserSiteOrganisation | NewUserSiteOrganisation;
  }

  resetForm(form: UserSiteOrganisationFormGroup, userSiteOrganisation: UserSiteOrganisationFormGroupInput): void {
    const userSiteOrganisationRawValue = { ...this.getFormDefaults(), ...userSiteOrganisation };
    form.reset(
      {
        ...userSiteOrganisationRawValue,
        id: { value: userSiteOrganisationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserSiteOrganisationFormDefaults {
    return {
      id: null,
      roles: [],
    };
  }
}
