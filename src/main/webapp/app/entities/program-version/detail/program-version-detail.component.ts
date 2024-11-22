import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProgramVersion } from '../program-version.model';

@Component({
  standalone: true,
  selector: 'jhi-program-version-detail',
  templateUrl: './program-version-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProgramVersionDetailComponent {
  programVersion = input<IProgramVersion | null>(null);

  previousState(): void {
    window.history.back();
  }
}
