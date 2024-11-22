import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAdditionalVariable, NewAdditionalVariable } from '../additional-variable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdditionalVariable for edit and NewAdditionalVariableFormGroupInput for create.
 */
type AdditionalVariableFormGroupInput = IAdditionalVariable | PartialWithRequiredKeyOf<NewAdditionalVariable>;

type AdditionalVariableFormDefaults = Pick<NewAdditionalVariable, 'id'>;

type AdditionalVariableFormGroupContent = {
  id: FormControl<IAdditionalVariable['id'] | NewAdditionalVariable['id']>;
  programVersionId: FormControl<IAdditionalVariable['programVersionId']>;
  jsonSchema: FormControl<IAdditionalVariable['jsonSchema']>;
  version: FormControl<IAdditionalVariable['version']>;
  site: FormControl<IAdditionalVariable['site']>;
};

export type AdditionalVariableFormGroup = FormGroup<AdditionalVariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdditionalVariableFormService {
  createAdditionalVariableFormGroup(additionalVariable: AdditionalVariableFormGroupInput = { id: null }): AdditionalVariableFormGroup {
    const additionalVariableRawValue = {
      ...this.getFormDefaults(),
      ...additionalVariable,
    };
    return new FormGroup<AdditionalVariableFormGroupContent>({
      id: new FormControl(
        { value: additionalVariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      programVersionId: new FormControl(additionalVariableRawValue.programVersionId, {
        validators: [Validators.required],
      }),
      jsonSchema: new FormControl(additionalVariableRawValue.jsonSchema, {
        validators: [Validators.required],
      }),
      version: new FormControl(additionalVariableRawValue.version),
      site: new FormControl(additionalVariableRawValue.site),
    });
  }

  getAdditionalVariable(form: AdditionalVariableFormGroup): IAdditionalVariable | NewAdditionalVariable {
    return form.getRawValue() as IAdditionalVariable | NewAdditionalVariable;
  }

  resetForm(form: AdditionalVariableFormGroup, additionalVariable: AdditionalVariableFormGroupInput): void {
    const additionalVariableRawValue = { ...this.getFormDefaults(), ...additionalVariable };
    form.reset(
      {
        ...additionalVariableRawValue,
        id: { value: additionalVariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdditionalVariableFormDefaults {
    return {
      id: null,
    };
  }
}
