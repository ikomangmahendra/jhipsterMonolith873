import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProgramVersion } from '../program-version.model';
import { ProgramVersionService } from '../service/program-version.service';

@Component({
  standalone: true,
  templateUrl: './program-version-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProgramVersionDeleteDialogComponent {
  programVersion?: IProgramVersion;

  protected programVersionService = inject(ProgramVersionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.programVersionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
