import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserSiteOrganisation } from '../user-site-organisation.model';
import { UserSiteOrganisationService } from '../service/user-site-organisation.service';

@Component({
  standalone: true,
  templateUrl: './user-site-organisation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserSiteOrganisationDeleteDialogComponent {
  userSiteOrganisation?: IUserSiteOrganisation;

  protected userSiteOrganisationService = inject(UserSiteOrganisationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userSiteOrganisationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
