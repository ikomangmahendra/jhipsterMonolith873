import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISiteOrganisation, NewSiteOrganisation } from '../site-organisation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISiteOrganisation for edit and NewSiteOrganisationFormGroupInput for create.
 */
type SiteOrganisationFormGroupInput = ISiteOrganisation | PartialWithRequiredKeyOf<NewSiteOrganisation>;

type SiteOrganisationFormDefaults = Pick<NewSiteOrganisation, 'id'>;

type SiteOrganisationFormGroupContent = {
  id: FormControl<ISiteOrganisation['id'] | NewSiteOrganisation['id']>;
};

export type SiteOrganisationFormGroup = FormGroup<SiteOrganisationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SiteOrganisationFormService {
  createSiteOrganisationFormGroup(siteOrganisation: SiteOrganisationFormGroupInput = { id: null }): SiteOrganisationFormGroup {
    const siteOrganisationRawValue = {
      ...this.getFormDefaults(),
      ...siteOrganisation,
    };
    return new FormGroup<SiteOrganisationFormGroupContent>({
      id: new FormControl(
        { value: siteOrganisationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
    });
  }

  getSiteOrganisation(form: SiteOrganisationFormGroup): NewSiteOrganisation {
    return form.getRawValue() as NewSiteOrganisation;
  }

  resetForm(form: SiteOrganisationFormGroup, siteOrganisation: SiteOrganisationFormGroupInput): void {
    const siteOrganisationRawValue = { ...this.getFormDefaults(), ...siteOrganisation };
    form.reset(
      {
        ...siteOrganisationRawValue,
        id: { value: siteOrganisationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SiteOrganisationFormDefaults {
    return {
      id: null,
    };
  }
}
