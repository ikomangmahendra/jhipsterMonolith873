import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISiteOrgTemp } from '../site-org-temp.model';
import { SiteOrgTempService } from '../service/site-org-temp.service';
import { SiteOrgTempFormGroup, SiteOrgTempFormService } from './site-org-temp-form.service';

@Component({
  standalone: true,
  selector: 'jhi-site-org-temp-update',
  templateUrl: './site-org-temp-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SiteOrgTempUpdateComponent implements OnInit {
  isSaving = false;
  siteOrgTemp: ISiteOrgTemp | null = null;

  protected siteOrgTempService = inject(SiteOrgTempService);
  protected siteOrgTempFormService = inject(SiteOrgTempFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SiteOrgTempFormGroup = this.siteOrgTempFormService.createSiteOrgTempFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteOrgTemp }) => {
      this.siteOrgTemp = siteOrgTemp;
      if (siteOrgTemp) {
        this.updateForm(siteOrgTemp);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const siteOrgTemp = this.siteOrgTempFormService.getSiteOrgTemp(this.editForm);
    this.subscribeToSaveResponse(this.siteOrgTempService.create(siteOrgTemp));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISiteOrgTemp>>): void {
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

  protected updateForm(siteOrgTemp: ISiteOrgTemp): void {
    this.siteOrgTemp = siteOrgTemp;
    this.siteOrgTempFormService.resetForm(this.editForm, siteOrgTemp);
  }
}
