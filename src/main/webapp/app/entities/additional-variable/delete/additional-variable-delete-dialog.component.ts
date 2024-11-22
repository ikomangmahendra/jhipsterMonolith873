import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAdditionalVariable } from '../additional-variable.model';
import { AdditionalVariableService } from '../service/additional-variable.service';

@Component({
  standalone: true,
  templateUrl: './additional-variable-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AdditionalVariableDeleteDialogComponent {
  additionalVariable?: IAdditionalVariable;

  protected additionalVariableService = inject(AdditionalVariableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.additionalVariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
