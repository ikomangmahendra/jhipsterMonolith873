export interface ISiteOrgTemp {
  id: number;
}

export type NewSiteOrgTemp = Omit<ISiteOrgTemp, 'id'> & { id: null };
