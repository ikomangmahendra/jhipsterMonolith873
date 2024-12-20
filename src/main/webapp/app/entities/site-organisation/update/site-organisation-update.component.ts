import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISiteOrganisation } from '../site-organisation.model';
import { SiteOrganisationService } from '../service/site-organisation.service';
import { SiteOrganisationFormGroup, SiteOrganisationFormService } from './site-organisation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-site-organisation-update',
  templateUrl: './site-organisation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SiteOrganisationUpdateComponent implements OnInit {
  isSaving = false;
  siteOrganisation: ISiteOrganisation | null = null;

  protected siteOrganisationService = inject(SiteOrganisationService);
  protected siteOrganisationFormService = inject(SiteOrganisationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SiteOrganisationFormGroup = this.siteOrganisationFormService.createSiteOrganisationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteOrganisation }) => {
      this.siteOrganisation = siteOrganisation;
      if (siteOrganisation) {
        this.updateForm(siteOrganisation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const siteOrganisation = this.siteOrganisationFormService.getSiteOrganisation(this.editForm);
    this.subscribeToSaveResponse(this.siteOrganisationService.create(siteOrganisation));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISiteOrganisation>>): void {
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

  protected updateForm(siteOrganisation: ISiteOrganisation): void {
    this.siteOrganisation = siteOrganisation;
    this.siteOrganisationFormService.resetForm(this.editForm, siteOrganisation);
  }
}
