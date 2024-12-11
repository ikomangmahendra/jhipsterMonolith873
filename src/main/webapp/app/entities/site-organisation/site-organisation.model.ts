export interface ISiteOrganisation {
  id: number;
}

export type NewSiteOrganisation = Omit<ISiteOrganisation, 'id'> & { id: null };
