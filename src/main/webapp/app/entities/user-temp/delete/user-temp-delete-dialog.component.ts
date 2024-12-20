import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserTemp } from '../user-temp.model';
import { UserTempService } from '../service/user-temp.service';

@Component({
  standalone: true,
  templateUrl: './user-temp-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserTempDeleteDialogComponent {
  userTemp?: IUserTemp;

  protected userTempService = inject(UserTempService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userTempService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
