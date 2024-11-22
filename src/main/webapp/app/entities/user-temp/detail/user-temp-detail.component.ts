import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserTemp } from '../user-temp.model';

@Component({
  standalone: true,
  selector: 'jhi-user-temp-detail',
  templateUrl: './user-temp-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserTempDetailComponent {
  userTemp = input<IUserTemp | null>(null);

  previousState(): void {
    window.history.back();
  }
}
