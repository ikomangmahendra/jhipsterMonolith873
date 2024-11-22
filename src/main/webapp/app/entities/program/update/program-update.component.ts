import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { UserTempService } from 'app/entities/user-temp/service/user-temp.service';
import { ProgramType } from 'app/entities/enumerations/program-type.model';
import { ProgramStatus } from 'app/entities/enumerations/program-status.model';
import { ProgramService } from '../service/program.service';
import { IProgram } from '../program.model';
import { ProgramFormGroup, ProgramFormService } from './program-form.service';

@Component({
  standalone: true,
  selector: 'jhi-program-update',
  templateUrl: './program-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProgramUpdateComponent implements OnInit {
  isSaving = false;
  program: IProgram | null = null;
  programTypeValues = Object.keys(ProgramType);
  programStatusValues = Object.keys(ProgramStatus);

  userTempsSharedCollection: IUserTemp[] = [];

  protected programService = inject(ProgramService);
  protected programFormService = inject(ProgramFormService);
  protected userTempService = inject(UserTempService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProgramFormGroup = this.programFormService.createProgramFormGroup();

  compareUserTemp = (o1: IUserTemp | null, o2: IUserTemp | null): boolean => this.userTempService.compareUserTemp(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ program }) => {
      this.program = program;
      if (program) {
        this.updateForm(program);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const program = this.programFormService.getProgram(this.editForm);
    if (program.id !== null) {
      this.subscribeToSaveResponse(this.programService.update(program));
    } else {
      this.subscribeToSaveResponse(this.programService.create(program));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgram>>): void {
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

  protected updateForm(program: IProgram): void {
    this.program = program;
    this.programFormService.resetForm(this.editForm, program);

    this.userTempsSharedCollection = this.userTempService.addUserTempToCollectionIfMissing<IUserTemp>(
      this.userTempsSharedCollection,
      ...(program.coordinators ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userTempService
      .query()
      .pipe(map((res: HttpResponse<IUserTemp[]>) => res.body ?? []))
      .pipe(
        map((userTemps: IUserTemp[]) =>
          this.userTempService.addUserTempToCollectionIfMissing<IUserTemp>(userTemps, ...(this.program?.coordinators ?? [])),
        ),
      )
      .subscribe((userTemps: IUserTemp[]) => (this.userTempsSharedCollection = userTemps));
  }
}
