import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISiteOrganisation } from '../site-organisation.model';
import { SiteOrganisationService } from '../service/site-organisation.service';

@Component({
  standalone: true,
  templateUrl: './site-organisation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SiteOrganisationDeleteDialogComponent {
  siteOrganisation?: ISiteOrganisation;

  protected siteOrganisationService = inject(SiteOrganisationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.siteOrganisationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
