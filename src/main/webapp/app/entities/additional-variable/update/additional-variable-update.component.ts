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
import { ISiteOrgTemp } from 'app/entities/site-org-temp/site-org-temp.model';
import { SiteOrgTempService } from 'app/entities/site-org-temp/service/site-org-temp.service';
import { AdditionalVariableService } from '../service/additional-variable.service';
import { IAdditionalVariable } from '../additional-variable.model';
import { AdditionalVariableFormGroup, AdditionalVariableFormService } from './additional-variable-form.service';

@Component({
  standalone: true,
  selector: 'jhi-additional-variable-update',
  templateUrl: './additional-variable-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AdditionalVariableUpdateComponent implements OnInit {
  isSaving = false;
  additionalVariable: IAdditionalVariable | null = null;

  programVersionsSharedCollection: IProgramVersion[] = [];
  siteOrgTempsSharedCollection: ISiteOrgTemp[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected additionalVariableService = inject(AdditionalVariableService);
  protected additionalVariableFormService = inject(AdditionalVariableFormService);
  protected programVersionService = inject(ProgramVersionService);
  protected siteOrgTempService = inject(SiteOrgTempService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AdditionalVariableFormGroup = this.additionalVariableFormService.createAdditionalVariableFormGroup();

  compareProgramVersion = (o1: IProgramVersion | null, o2: IProgramVersion | null): boolean =>
    this.programVersionService.compareProgramVersion(o1, o2);

  compareSiteOrgTemp = (o1: ISiteOrgTemp | null, o2: ISiteOrgTemp | null): boolean => this.siteOrgTempService.compareSiteOrgTemp(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ additionalVariable }) => {
      this.additionalVariable = additionalVariable;
      if (additionalVariable) {
        this.updateForm(additionalVariable);
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
    const additionalVariable = this.additionalVariableFormService.getAdditionalVariable(this.editForm);
    if (additionalVariable.id !== null) {
      this.subscribeToSaveResponse(this.additionalVariableService.update(additionalVariable));
    } else {
      this.subscribeToSaveResponse(this.additionalVariableService.create(additionalVariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdditionalVariable>>): void {
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

  protected updateForm(additionalVariable: IAdditionalVariable): void {
    this.additionalVariable = additionalVariable;
    this.additionalVariableFormService.resetForm(this.editForm, additionalVariable);

    this.programVersionsSharedCollection = this.programVersionService.addProgramVersionToCollectionIfMissing<IProgramVersion>(
      this.programVersionsSharedCollection,
      additionalVariable.version,
    );
    this.siteOrgTempsSharedCollection = this.siteOrgTempService.addSiteOrgTempToCollectionIfMissing<ISiteOrgTemp>(
      this.siteOrgTempsSharedCollection,
      additionalVariable.site,
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
            this.additionalVariable?.version,
          ),
        ),
      )
      .subscribe((programVersions: IProgramVersion[]) => (this.programVersionsSharedCollection = programVersions));

    this.siteOrgTempService
      .query()
      .pipe(map((res: HttpResponse<ISiteOrgTemp[]>) => res.body ?? []))
      .pipe(
        map((siteOrgTemps: ISiteOrgTemp[]) =>
          this.siteOrgTempService.addSiteOrgTempToCollectionIfMissing<ISiteOrgTemp>(siteOrgTemps, this.additionalVariable?.site),
        ),
      )
      .subscribe((siteOrgTemps: ISiteOrgTemp[]) => (this.siteOrgTempsSharedCollection = siteOrgTemps));
  }
}
