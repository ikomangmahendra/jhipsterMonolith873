import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUserTemp, NewUserTemp } from '../user-temp.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserTemp for edit and NewUserTempFormGroupInput for create.
 */
type UserTempFormGroupInput = IUserTemp | PartialWithRequiredKeyOf<NewUserTemp>;

type UserTempFormDefaults = Pick<NewUserTemp, 'id'>;

type UserTempFormGroupContent = {
  id: FormControl<IUserTemp['id'] | NewUserTemp['id']>;
};

export type UserTempFormGroup = FormGroup<UserTempFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserTempFormService {
  createUserTempFormGroup(userTemp: UserTempFormGroupInput = { id: null }): UserTempFormGroup {
    const userTempRawValue = {
      ...this.getFormDefaults(),
      ...userTemp,
    };
    return new FormGroup<UserTempFormGroupContent>({
      id: new FormControl(
        { value: userTempRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
    });
  }

  getUserTemp(form: UserTempFormGroup): NewUserTemp {
    return form.getRawValue() as NewUserTemp;
  }

  resetForm(form: UserTempFormGroup, userTemp: UserTempFormGroupInput): void {
    const userTempRawValue = { ...this.getFormDefaults(), ...userTemp };
    form.reset(
      {
        ...userTempRawValue,
        id: { value: userTempRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserTempFormDefaults {
    return {
      id: null,
    };
  }
}
