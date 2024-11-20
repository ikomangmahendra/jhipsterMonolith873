import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConsumer, NewConsumer } from '../consumer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsumer for edit and NewConsumerFormGroupInput for create.
 */
type ConsumerFormGroupInput = IConsumer | PartialWithRequiredKeyOf<NewConsumer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConsumer | NewConsumer> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ConsumerFormRawValue = FormValueOf<IConsumer>;

type NewConsumerFormRawValue = FormValueOf<NewConsumer>;

type ConsumerFormDefaults = Pick<NewConsumer, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ConsumerFormGroupContent = {
  id: FormControl<ConsumerFormRawValue['id'] | NewConsumer['id']>;
  guid: FormControl<ConsumerFormRawValue['guid']>;
  note: FormControl<ConsumerFormRawValue['note']>;
  createdBy: FormControl<ConsumerFormRawValue['createdBy']>;
  createdDate: FormControl<ConsumerFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ConsumerFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ConsumerFormRawValue['lastModifiedDate']>;
  recordStatusId: FormControl<ConsumerFormRawValue['recordStatusId']>;
};

export type ConsumerFormGroup = FormGroup<ConsumerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConsumerFormService {
  createConsumerFormGroup(consumer: ConsumerFormGroupInput = { id: null }): ConsumerFormGroup {
    const consumerRawValue = this.convertConsumerToConsumerRawValue({
      ...this.getFormDefaults(),
      ...consumer,
    });
    return new FormGroup<ConsumerFormGroupContent>({
      id: new FormControl(
        { value: consumerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      guid: new FormControl(consumerRawValue.guid, {
        validators: [Validators.required, Validators.maxLength(38)],
      }),
      note: new FormControl(consumerRawValue.note, {
        validators: [Validators.maxLength(250)],
      }),
      createdBy: new FormControl(consumerRawValue.createdBy, {
        validators: [Validators.maxLength(50)],
      }),
      createdDate: new FormControl(consumerRawValue.createdDate),
      lastModifiedBy: new FormControl(consumerRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(consumerRawValue.lastModifiedDate),
      recordStatusId: new FormControl(consumerRawValue.recordStatusId, {
        validators: [Validators.required],
      }),
    });
  }

  getConsumer(form: ConsumerFormGroup): IConsumer | NewConsumer {
    return this.convertConsumerRawValueToConsumer(form.getRawValue() as ConsumerFormRawValue | NewConsumerFormRawValue);
  }

  resetForm(form: ConsumerFormGroup, consumer: ConsumerFormGroupInput): void {
    const consumerRawValue = this.convertConsumerToConsumerRawValue({ ...this.getFormDefaults(), ...consumer });
    form.reset(
      {
        ...consumerRawValue,
        id: { value: consumerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConsumerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertConsumerRawValueToConsumer(rawConsumer: ConsumerFormRawValue | NewConsumerFormRawValue): IConsumer | NewConsumer {
    return {
      ...rawConsumer,
      createdDate: dayjs(rawConsumer.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawConsumer.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertConsumerToConsumerRawValue(
    consumer: IConsumer | (Partial<NewConsumer> & ConsumerFormDefaults),
  ): ConsumerFormRawValue | PartialWithRequiredKeyOf<NewConsumerFormRawValue> {
    return {
      ...consumer,
      createdDate: consumer.createdDate ? consumer.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: consumer.lastModifiedDate ? consumer.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
