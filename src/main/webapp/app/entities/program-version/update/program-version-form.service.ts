import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProgramVersion, NewProgramVersion } from '../program-version.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProgramVersion for edit and NewProgramVersionFormGroupInput for create.
 */
type ProgramVersionFormGroupInput = IProgramVersion | PartialWithRequiredKeyOf<NewProgramVersion>;

type ProgramVersionFormDefaults = Pick<NewProgramVersion, 'id' | 'isActive'>;

type ProgramVersionFormGroupContent = {
  id: FormControl<IProgramVersion['id'] | NewProgramVersion['id']>;
  version: FormControl<IProgramVersion['version']>;
  isActive: FormControl<IProgramVersion['isActive']>;
  publishDate: FormControl<IProgramVersion['publishDate']>;
  program: FormControl<IProgramVersion['program']>;
};

export type ProgramVersionFormGroup = FormGroup<ProgramVersionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProgramVersionFormService {
  createProgramVersionFormGroup(programVersion: ProgramVersionFormGroupInput = { id: null }): ProgramVersionFormGroup {
    const programVersionRawValue = {
      ...this.getFormDefaults(),
      ...programVersion,
    };
    return new FormGroup<ProgramVersionFormGroupContent>({
      id: new FormControl(
        { value: programVersionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      version: new FormControl(programVersionRawValue.version, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(programVersionRawValue.isActive, {
        validators: [Validators.required],
      }),
      publishDate: new FormControl(programVersionRawValue.publishDate),
      program: new FormControl(programVersionRawValue.program),
    });
  }

  getProgramVersion(form: ProgramVersionFormGroup): IProgramVersion | NewProgramVersion {
    return form.getRawValue() as IProgramVersion | NewProgramVersion;
  }

  resetForm(form: ProgramVersionFormGroup, programVersion: ProgramVersionFormGroupInput): void {
    const programVersionRawValue = { ...this.getFormDefaults(), ...programVersion };
    form.reset(
      {
        ...programVersionRawValue,
        id: { value: programVersionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProgramVersionFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
