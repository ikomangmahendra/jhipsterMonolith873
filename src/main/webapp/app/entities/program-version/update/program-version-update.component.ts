import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProgram } from 'app/entities/program/program.model';
import { ProgramService } from 'app/entities/program/service/program.service';
import { IProgramVersion } from '../program-version.model';
import { ProgramVersionService } from '../service/program-version.service';
import { ProgramVersionFormGroup, ProgramVersionFormService } from './program-version-form.service';

@Component({
  standalone: true,
  selector: 'jhi-program-version-update',
  templateUrl: './program-version-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProgramVersionUpdateComponent implements OnInit {
  isSaving = false;
  programVersion: IProgramVersion | null = null;

  programsSharedCollection: IProgram[] = [];

  protected programVersionService = inject(ProgramVersionService);
  protected programVersionFormService = inject(ProgramVersionFormService);
  protected programService = inject(ProgramService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProgramVersionFormGroup = this.programVersionFormService.createProgramVersionFormGroup();

  compareProgram = (o1: IProgram | null, o2: IProgram | null): boolean => this.programService.compareProgram(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programVersion }) => {
      this.programVersion = programVersion;
      if (programVersion) {
        this.updateForm(programVersion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const programVersion = this.programVersionFormService.getProgramVersion(this.editForm);
    if (programVersion.id !== null) {
      this.subscribeToSaveResponse(this.programVersionService.update(programVersion));
    } else {
      this.subscribeToSaveResponse(this.programVersionService.create(programVersion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgramVersion>>): void {
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

  protected updateForm(programVersion: IProgramVersion): void {
    this.programVersion = programVersion;
    this.programVersionFormService.resetForm(this.editForm, programVersion);

    this.programsSharedCollection = this.programService.addProgramToCollectionIfMissing<IProgram>(
      this.programsSharedCollection,
      programVersion.program,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.programService
      .query()
      .pipe(map((res: HttpResponse<IProgram[]>) => res.body ?? []))
      .pipe(
        map((programs: IProgram[]) =>
          this.programService.addProgramToCollectionIfMissing<IProgram>(programs, this.programVersion?.program),
        ),
      )
      .subscribe((programs: IProgram[]) => (this.programsSharedCollection = programs));
  }
}
