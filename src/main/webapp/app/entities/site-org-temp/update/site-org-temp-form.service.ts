import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISiteOrgTemp, NewSiteOrgTemp } from '../site-org-temp.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISiteOrgTemp for edit and NewSiteOrgTempFormGroupInput for create.
 */
type SiteOrgTempFormGroupInput = ISiteOrgTemp | PartialWithRequiredKeyOf<NewSiteOrgTemp>;

type SiteOrgTempFormDefaults = Pick<NewSiteOrgTemp, 'id'>;

type SiteOrgTempFormGroupContent = {
  id: FormControl<ISiteOrgTemp['id'] | NewSiteOrgTemp['id']>;
};

export type SiteOrgTempFormGroup = FormGroup<SiteOrgTempFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SiteOrgTempFormService {
  createSiteOrgTempFormGroup(siteOrgTemp: SiteOrgTempFormGroupInput = { id: null }): SiteOrgTempFormGroup {
    const siteOrgTempRawValue = {
      ...this.getFormDefaults(),
      ...siteOrgTemp,
    };
    return new FormGroup<SiteOrgTempFormGroupContent>({
      id: new FormControl(
        { value: siteOrgTempRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
    });
  }

  getSiteOrgTemp(form: SiteOrgTempFormGroup): NewSiteOrgTemp {
    return form.getRawValue() as NewSiteOrgTemp;
  }

  resetForm(form: SiteOrgTempFormGroup, siteOrgTemp: SiteOrgTempFormGroupInput): void {
    const siteOrgTempRawValue = { ...this.getFormDefaults(), ...siteOrgTemp };
    form.reset(
      {
        ...siteOrgTempRawValue,
        id: { value: siteOrgTempRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SiteOrgTempFormDefaults {
    return {
      id: null,
    };
  }
}
