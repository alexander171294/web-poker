export class ReactionEvents {
  public type: RxEType;
  public data: any;

  public constructor(type: RxEType, data: any) {
    this.type = type;
    this.data = data;
  }
}

export enum RxEType {
  DEPOSIT_SUCCESS,
  DEPOSIT_ANNOUNCEMENT,
  START_IN,
  ANNOUNCEMENT,
  ROUND_START,
  BLINDS,
  CARD_DIST,
  ME_CARD_DIST,
  WAITING_FOR,
  INGRESS,
  BET_DECISION,
  DONE_ACTION,
  FLOP,
  RIVER,
  TURN,
  SNAPSHOT,
  DEFINE_POSITION,
  DECISION_INFORM,
  SHOW_OFF,
  RESULT_SET,
  POTS,
  FOLD,
  CHIP_STATUS,
  CHAT,
  LEAVE
}

