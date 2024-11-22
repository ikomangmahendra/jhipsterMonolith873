import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccuteProgramVariable } from '../accute-program-variable.model';
import { AccuteProgramVariableService } from '../service/accute-program-variable.service';

@Component({
  standalone: true,
  templateUrl: './accute-program-variable-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccuteProgramVariableDeleteDialogComponent {
  accuteProgramVariable?: IAccuteProgramVariable;

  protected accuteProgramVariableService = inject(AccuteProgramVariableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accuteProgramVariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
