import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISiteOrgTemp } from '../site-org-temp.model';
import { SiteOrgTempService } from '../service/site-org-temp.service';

@Component({
  standalone: true,
  templateUrl: './site-org-temp-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SiteOrgTempDeleteDialogComponent {
  siteOrgTemp?: ISiteOrgTemp;

  protected siteOrgTempService = inject(SiteOrgTempService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.siteOrgTempService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
