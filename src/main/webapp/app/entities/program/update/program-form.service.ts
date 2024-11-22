import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProgram, NewProgram } from '../program.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProgram for edit and NewProgramFormGroupInput for create.
 */
type ProgramFormGroupInput = IProgram | PartialWithRequiredKeyOf<NewProgram>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProgram | NewProgram> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type ProgramFormRawValue = FormValueOf<IProgram>;

type NewProgramFormRawValue = FormValueOf<NewProgram>;

type ProgramFormDefaults = Pick<
  NewProgram,
  'id' | 'startDate' | 'endDate' | 'isEnableFollowUp' | 'isNsfSurveyAccess' | 'isOptOutAllowed' | 'coordinators'
>;

type ProgramFormGroupContent = {
  id: FormControl<ProgramFormRawValue['id'] | NewProgram['id']>;
  name: FormControl<ProgramFormRawValue['name']>;
  type: FormControl<ProgramFormRawValue['type']>;
  startDate: FormControl<ProgramFormRawValue['startDate']>;
  endDate: FormControl<ProgramFormRawValue['endDate']>;
  status: FormControl<ProgramFormRawValue['status']>;
  externalSystemLkp: FormControl<ProgramFormRawValue['externalSystemLkp']>;
  isEnableFollowUp: FormControl<ProgramFormRawValue['isEnableFollowUp']>;
  isNsfSurveyAccess: FormControl<ProgramFormRawValue['isNsfSurveyAccess']>;
  isOptOutAllowed: FormControl<ProgramFormRawValue['isOptOutAllowed']>;
  coordinators: FormControl<ProgramFormRawValue['coordinators']>;
};

export type ProgramFormGroup = FormGroup<ProgramFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProgramFormService {
  createProgramFormGroup(program: ProgramFormGroupInput = { id: null }): ProgramFormGroup {
    const programRawValue = this.convertProgramToProgramRawValue({
      ...this.getFormDefaults(),
      ...program,
    });
    return new FormGroup<ProgramFormGroupContent>({
      id: new FormControl(
        { value: programRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(programRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      type: new FormControl(programRawValue.type, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(programRawValue.startDate),
      endDate: new FormControl(programRawValue.endDate),
      status: new FormControl(programRawValue.status, {
        validators: [Validators.required],
      }),
      externalSystemLkp: new FormControl(programRawValue.externalSystemLkp, {
        validators: [Validators.required],
      }),
      isEnableFollowUp: new FormControl(programRawValue.isEnableFollowUp, {
        validators: [Validators.required],
      }),
      isNsfSurveyAccess: new FormControl(programRawValue.isNsfSurveyAccess, {
        validators: [Validators.required],
      }),
      isOptOutAllowed: new FormControl(programRawValue.isOptOutAllowed, {
        validators: [Validators.required],
      }),
      coordinators: new FormControl(programRawValue.coordinators ?? []),
    });
  }

  getProgram(form: ProgramFormGroup): IProgram | NewProgram {
    return this.convertProgramRawValueToProgram(form.getRawValue() as ProgramFormRawValue | NewProgramFormRawValue);
  }

  resetForm(form: ProgramFormGroup, program: ProgramFormGroupInput): void {
    const programRawValue = this.convertProgramToProgramRawValue({ ...this.getFormDefaults(), ...program });
    form.reset(
      {
        ...programRawValue,
        id: { value: programRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProgramFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      isEnableFollowUp: false,
      isNsfSurveyAccess: false,
      isOptOutAllowed: false,
      coordinators: [],
    };
  }

  private convertProgramRawValueToProgram(rawProgram: ProgramFormRawValue | NewProgramFormRawValue): IProgram | NewProgram {
    return {
      ...rawProgram,
      startDate: dayjs(rawProgram.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawProgram.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertProgramToProgramRawValue(
    program: IProgram | (Partial<NewProgram> & ProgramFormDefaults),
  ): ProgramFormRawValue | PartialWithRequiredKeyOf<NewProgramFormRawValue> {
    return {
      ...program,
      startDate: program.startDate ? program.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: program.endDate ? program.endDate.format(DATE_TIME_FORMAT) : undefined,
      coordinators: program.coordinators ?? [],
    };
  }
}
