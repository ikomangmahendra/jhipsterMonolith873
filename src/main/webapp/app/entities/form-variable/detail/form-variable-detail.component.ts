import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IFormVariable } from '../form-variable.model';

@Component({
  standalone: true,
  selector: 'jhi-form-variable-detail',
  templateUrl: './form-variable-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FormVariableDetailComponent {
  formVariable = input<IFormVariable | null>(null);

  previousState(): void {
    window.history.back();
  }
}
