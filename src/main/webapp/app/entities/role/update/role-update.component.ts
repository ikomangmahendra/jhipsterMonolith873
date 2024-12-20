import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserTemp } from 'app/entities/user-temp/user-temp.model';
import { UserTempService } from 'app/entities/user-temp/service/user-temp.service';
import { IUserSiteOrganisation } from 'app/entities/user-site-organisation/user-site-organisation.model';
import { UserSiteOrganisationService } from 'app/entities/user-site-organisation/service/user-site-organisation.service';
import { RoleService } from '../service/role.service';
import { IRole } from '../role.model';
import { RoleFormGroup, RoleFormService } from './role-form.service';

@Component({
  standalone: true,
  selector: 'jhi-role-update',
  templateUrl: './role-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoleUpdateComponent implements OnInit {
  isSaving = false;
  role: IRole | null = null;

  userTempsSharedCollection: IUserTemp[] = [];
  userSiteOrganisationsSharedCollection: IUserSiteOrganisation[] = [];

  protected roleService = inject(RoleService);
  protected roleFormService = inject(RoleFormService);
  protected userTempService = inject(UserTempService);
  protected userSiteOrganisationService = inject(UserSiteOrganisationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoleFormGroup = this.roleFormService.createRoleFormGroup();

  compareUserTemp = (o1: IUserTemp | null, o2: IUserTemp | null): boolean => this.userTempService.compareUserTemp(o1, o2);

  compareUserSiteOrganisation = (o1: IUserSiteOrganisation | null, o2: IUserSiteOrganisation | null): boolean =>
    this.userSiteOrganisationService.compareUserSiteOrganisation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.role = role;
      if (role) {
        this.updateForm(role);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const role = this.roleFormService.getRole(this.editForm);
    if (role.id !== null) {
      this.subscribeToSaveResponse(this.roleService.update(role));
    } else {
      this.subscribeToSaveResponse(this.roleService.create(role));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRole>>): void {
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

  protected updateForm(role: IRole): void {
    this.role = role;
    this.roleFormService.resetForm(this.editForm, role);

    this.userTempsSharedCollection = this.userTempService.addUserTempToCollectionIfMissing<IUserTemp>(
      this.userTempsSharedCollection,
      role.user,
    );
    this.userSiteOrganisationsSharedCollection =
      this.userSiteOrganisationService.addUserSiteOrganisationToCollectionIfMissing<IUserSiteOrganisation>(
        this.userSiteOrganisationsSharedCollection,
        ...(role.userSiteOrganisations ?? []),
      );
  }

  protected loadRelationshipsOptions(): void {
    this.userTempService
      .query()
      .pipe(map((res: HttpResponse<IUserTemp[]>) => res.body ?? []))
      .pipe(map((userTemps: IUserTemp[]) => this.userTempService.addUserTempToCollectionIfMissing<IUserTemp>(userTemps, this.role?.user)))
      .subscribe((userTemps: IUserTemp[]) => (this.userTempsSharedCollection = userTemps));

    this.userSiteOrganisationService
      .query()
      .pipe(map((res: HttpResponse<IUserSiteOrganisation[]>) => res.body ?? []))
      .pipe(
        map((userSiteOrganisations: IUserSiteOrganisation[]) =>
          this.userSiteOrganisationService.addUserSiteOrganisationToCollectionIfMissing<IUserSiteOrganisation>(
            userSiteOrganisations,
            ...(this.role?.userSiteOrganisations ?? []),
          ),
        ),
      )
      .subscribe((userSiteOrganisations: IUserSiteOrganisation[]) => (this.userSiteOrganisationsSharedCollection = userSiteOrganisations));
  }
}
