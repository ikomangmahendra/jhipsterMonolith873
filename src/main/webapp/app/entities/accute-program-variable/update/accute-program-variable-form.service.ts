import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAccuteProgramVariable, NewAccuteProgramVariable } from '../accute-program-variable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccuteProgramVariable for edit and NewAccuteProgramVariableFormGroupInput for create.
 */
type AccuteProgramVariableFormGroupInput = IAccuteProgramVariable | PartialWithRequiredKeyOf<NewAccuteProgramVariable>;

type AccuteProgramVariableFormDefaults = Pick<NewAccuteProgramVariable, 'id'>;

type AccuteProgramVariableFormGroupContent = {
  id: FormControl<IAccuteProgramVariable['id'] | NewAccuteProgramVariable['id']>;
  sectionId: FormControl<IAccuteProgramVariable['sectionId']>;
  sectionName: FormControl<IAccuteProgramVariable['sectionName']>;
  orderIndex: FormControl<IAccuteProgramVariable['orderIndex']>;
  jsonSchema: FormControl<IAccuteProgramVariable['jsonSchema']>;
  version: FormControl<IAccuteProgramVariable['version']>;
};

export type AccuteProgramVariableFormGroup = FormGroup<AccuteProgramVariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccuteProgramVariableFormService {
  createAccuteProgramVariableFormGroup(
    accuteProgramVariable: AccuteProgramVariableFormGroupInput = { id: null },
  ): AccuteProgramVariableFormGroup {
    const accuteProgramVariableRawValue = {
      ...this.getFormDefaults(),
      ...accuteProgramVariable,
    };
    return new FormGroup<AccuteProgramVariableFormGroupContent>({
      id: new FormControl(
        { value: accuteProgramVariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sectionId: new FormControl(accuteProgramVariableRawValue.sectionId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      sectionName: new FormControl(accuteProgramVariableRawValue.sectionName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      orderIndex: new FormControl(accuteProgramVariableRawValue.orderIndex, {
        validators: [Validators.required],
      }),
      jsonSchema: new FormControl(accuteProgramVariableRawValue.jsonSchema, {
        validators: [Validators.required],
      }),
      version: new FormControl(accuteProgramVariableRawValue.version),
    });
  }

  getAccuteProgramVariable(form: AccuteProgramVariableFormGroup): IAccuteProgramVariable | NewAccuteProgramVariable {
    return form.getRawValue() as IAccuteProgramVariable | NewAccuteProgramVariable;
  }

  resetForm(form: AccuteProgramVariableFormGroup, accuteProgramVariable: AccuteProgramVariableFormGroupInput): void {
    const accuteProgramVariableRawValue = { ...this.getFormDefaults(), ...accuteProgramVariable };
    form.reset(
      {
        ...accuteProgramVariableRawValue,
        id: { value: accuteProgramVariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccuteProgramVariableFormDefaults {
    return {
      id: null,
    };
  }
}
