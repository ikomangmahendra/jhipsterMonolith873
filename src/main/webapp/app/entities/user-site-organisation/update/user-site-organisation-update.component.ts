import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { UserTempService } from 'app/entities/user-temp/service/user-temp.service';
import { ISiteOrganisation } from 'app/entities/site-organisation/site-organisation.model';
import { SiteOrganisationService } from 'app/entities/site-organisation/service/site-organisation.service';
import { UserSiteOrganisationService } from '../service/user-site-organisation.service';
import { IUserSiteOrganisation } from '../user-site-organisation.model';
import { UserSiteOrganisationFormGroup, UserSiteOrganisationFormService } from './user-site-organisation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-site-organisation-update',
  templateUrl: './user-site-organisation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserSiteOrganisationUpdateComponent implements OnInit {
  isSaving = false;
  userSiteOrganisation: IUserSiteOrganisation | null = null;

  rolesSharedCollection: IRole[] = [];
  userTempsSharedCollection: IUserTemp[] = [];
  siteOrganisationsSharedCollection: ISiteOrganisation[] = [];

  protected userSiteOrganisationService = inject(UserSiteOrganisationService);
  protected userSiteOrganisationFormService = inject(UserSiteOrganisationFormService);
  protected roleService = inject(RoleService);
  protected userTempService = inject(UserTempService);
  protected siteOrganisationService = inject(SiteOrganisationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserSiteOrganisationFormGroup = this.userSiteOrganisationFormService.createUserSiteOrganisationFormGroup();

  compareRole = (o1: IRole | null, o2: IRole | null): boolean => this.roleService.compareRole(o1, o2);

  compareUserTemp = (o1: IUserTemp | null, o2: IUserTemp | null): boolean => this.userTempService.compareUserTemp(o1, o2);

  compareSiteOrganisation = (o1: ISiteOrganisation | null, o2: ISiteOrganisation | null): boolean =>
    this.siteOrganisationService.compareSiteOrganisation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userSiteOrganisation }) => {
      this.userSiteOrganisation = userSiteOrganisation;
      if (userSiteOrganisation) {
        this.updateForm(userSiteOrganisation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userSiteOrganisation = this.userSiteOrganisationFormService.getUserSiteOrganisation(this.editForm);
    if (userSiteOrganisation.id !== null) {
      this.subscribeToSaveResponse(this.userSiteOrganisationService.update(userSiteOrganisation));
    } else {
      this.subscribeToSaveResponse(this.userSiteOrganisationService.create(userSiteOrganisation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserSiteOrganisation>>): void {
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

  protected updateForm(userSiteOrganisation: IUserSiteOrganisation): void {
    this.userSiteOrganisation = userSiteOrganisation;
    this.userSiteOrganisationFormService.resetForm(this.editForm, userSiteOrganisation);

    this.rolesSharedCollection = this.roleService.addRoleToCollectionIfMissing<IRole>(
      this.rolesSharedCollection,
      ...(userSiteOrganisation.roles ?? []),
    );
    this.userTempsSharedCollection = this.userTempService.addUserTempToCollectionIfMissing<IUserTemp>(
      this.userTempsSharedCollection,
      userSiteOrganisation.user,
    );
    this.siteOrganisationsSharedCollection = this.siteOrganisationService.addSiteOrganisationToCollectionIfMissing<ISiteOrganisation>(
      this.siteOrganisationsSharedCollection,
      userSiteOrganisation.siteOrganisation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .pipe(
        map((roles: IRole[]) => this.roleService.addRoleToCollectionIfMissing<IRole>(roles, ...(this.userSiteOrganisation?.roles ?? []))),
      )
      .subscribe((roles: IRole[]) => (this.rolesSharedCollection = roles));

    this.userTempService
      .query()
      .pipe(map((res: HttpResponse<IUserTemp[]>) => res.body ?? []))
      .pipe(
        map((userTemps: IUserTemp[]) =>
          this.userTempService.addUserTempToCollectionIfMissing<IUserTemp>(userTemps, this.userSiteOrganisation?.user),
        ),
      )
      .subscribe((userTemps: IUserTemp[]) => (this.userTempsSharedCollection = userTemps));

    this.siteOrganisationService
      .query()
      .pipe(map((res: HttpResponse<ISiteOrganisation[]>) => res.body ?? []))
      .pipe(
        map((siteOrganisations: ISiteOrganisation[]) =>
          this.siteOrganisationService.addSiteOrganisationToCollectionIfMissing<ISiteOrganisation>(
            siteOrganisations,
            this.userSiteOrganisation?.siteOrganisation,
          ),
        ),
      )
      .subscribe((siteOrganisations: ISiteOrganisation[]) => (this.siteOrganisationsSharedCollection = siteOrganisations));
  }
}
