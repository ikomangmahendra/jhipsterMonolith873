import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProgramVersion } from 'app/entities/program-version/program-version.model';
import { ProgramVersionService } from 'app/entities/program-version/service/program-version.service';
import { AccuteProgramVariableService } from '../service/accute-program-variable.service';
import { IAccuteProgramVariable } from '../accute-program-variable.model';
import { AccuteProgramVariableFormGroup, AccuteProgramVariableFormService } from './accute-program-variable-form.service';

@Component({
  standalone: true,
  selector: 'jhi-accute-program-variable-update',
  templateUrl: './accute-program-variable-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccuteProgramVariableUpdateComponent implements OnInit {
  isSaving = false;
  accuteProgramVariable: IAccuteProgramVariable | null = null;

  programVersionsSharedCollection: IProgramVersion[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected accuteProgramVariableService = inject(AccuteProgramVariableService);
  protected accuteProgramVariableFormService = inject(AccuteProgramVariableFormService);
  protected programVersionService = inject(ProgramVersionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccuteProgramVariableFormGroup = this.accuteProgramVariableFormService.createAccuteProgramVariableFormGroup();

  compareProgramVersion = (o1: IProgramVersion | null, o2: IProgramVersion | null): boolean =>
    this.programVersionService.compareProgramVersion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accuteProgramVariable }) => {
      this.accuteProgramVariable = accuteProgramVariable;
      if (accuteProgramVariable) {
        this.updateForm(accuteProgramVariable);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('jhipsterMonolith873App.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accuteProgramVariable = this.accuteProgramVariableFormService.getAccuteProgramVariable(this.editForm);
    if (accuteProgramVariable.id !== null) {
      this.subscribeToSaveResponse(this.accuteProgramVariableService.update(accuteProgramVariable));
    } else {
      this.subscribeToSaveResponse(this.accuteProgramVariableService.create(accuteProgramVariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccuteProgramVariable>>): void {
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

  protected updateForm(accuteProgramVariable: IAccuteProgramVariable): void {
    this.accuteProgramVariable = accuteProgramVariable;
    this.accuteProgramVariableFormService.resetForm(this.editForm, accuteProgramVariable);

    this.programVersionsSharedCollection = this.programVersionService.addProgramVersionToCollectionIfMissing<IProgramVersion>(
      this.programVersionsSharedCollection,
      accuteProgramVariable.version,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.programVersionService
      .query()
      .pipe(map((res: HttpResponse<IProgramVersion[]>) => res.body ?? []))
      .pipe(
        map((programVersions: IProgramVersion[]) =>
          this.programVersionService.addProgramVersionToCollectionIfMissing<IProgramVersion>(
            programVersions,
            this.accuteProgramVariable?.version,
          ),
        ),
      )
      .subscribe((programVersions: IProgramVersion[]) => (this.programVersionsSharedCollection = programVersions));
  }
}
