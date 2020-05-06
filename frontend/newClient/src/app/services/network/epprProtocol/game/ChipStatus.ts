import { InGameSchema } from './InGameSchema';
export class ChipStatus extends InGameSchema {
  public status: IndividualChipStatus[];
}

export class IndividualChipStatus {
  public position: number;
  public chips: number;
}
