import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IRole } from '../role.model';

@Component({
  standalone: true,
  selector: 'jhi-role-detail',
  templateUrl: './role-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoleDetailComponent {
  role = input<IRole | null>(null);

  previousState(): void {
    window.history.back();
  }
}
