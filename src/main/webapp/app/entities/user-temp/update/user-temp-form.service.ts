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

type UserTempFormDefaults = Pick<NewUserTemp, 'id' | 'programs'>;

type UserTempFormGroupContent = {
  id: FormControl<IUserTemp['id'] | NewUserTemp['id']>;
  programs: FormControl<IUserTemp['programs']>;
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
      programs: new FormControl(userTempRawValue.programs ?? []),
    });
  }

  getUserTemp(form: UserTempFormGroup): IUserTemp | NewUserTemp {
    return form.getRawValue() as IUserTemp | NewUserTemp;
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
      programs: [],
    };
  }
}
