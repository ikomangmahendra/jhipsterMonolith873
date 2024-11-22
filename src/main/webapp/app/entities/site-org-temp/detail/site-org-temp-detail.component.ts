import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISiteOrgTemp } from '../site-org-temp.model';

@Component({
  standalone: true,
  selector: 'jhi-site-org-temp-detail',
  templateUrl: './site-org-temp-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SiteOrgTempDetailComponent {
  siteOrgTemp = input<ISiteOrgTemp | null>(null);

  previousState(): void {
    window.history.back();
  }
}
