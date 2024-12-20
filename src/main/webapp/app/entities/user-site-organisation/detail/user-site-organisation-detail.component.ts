import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserSiteOrganisation } from '../user-site-organisation.model';

@Component({
  standalone: true,
  selector: 'jhi-user-site-organisation-detail',
  templateUrl: './user-site-organisation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserSiteOrganisationDetailComponent {
  userSiteOrganisation = input<IUserSiteOrganisation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
