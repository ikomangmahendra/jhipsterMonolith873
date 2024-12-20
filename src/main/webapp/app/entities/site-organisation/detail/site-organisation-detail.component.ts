import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISiteOrganisation } from '../site-organisation.model';

@Component({
  standalone: true,
  selector: 'jhi-site-organisation-detail',
  templateUrl: './site-organisation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SiteOrganisationDetailComponent {
  siteOrganisation = input<ISiteOrganisation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
