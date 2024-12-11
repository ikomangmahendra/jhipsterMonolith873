import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserTemp } from '../user-temp.model';
import { UserTempService } from '../service/user-temp.service';
import { UserTempFormGroup, UserTempFormService } from './user-temp-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-temp-update',
  templateUrl: './user-temp-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserTempUpdateComponent implements OnInit {
  isSaving = false;
  userTemp: IUserTemp | null = null;

  protected userTempService = inject(UserTempService);
  protected userTempFormService = inject(UserTempFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserTempFormGroup = this.userTempFormService.createUserTempFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userTemp }) => {
      this.userTemp = userTemp;
      if (userTemp) {
        this.updateForm(userTemp);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userTemp = this.userTempFormService.getUserTemp(this.editForm);
    this.subscribeToSaveResponse(this.userTempService.create(userTemp));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserTemp>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userTemp: IUserTemp): void {
    this.userTemp = userTemp;
    this.userTempFormService.resetForm(this.editForm, userTemp);
  }
}
