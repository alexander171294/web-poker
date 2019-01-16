export class SessionData {

    // Clave secreta
    public Secret: string;
    // Fecha expiración
    public Expiration: number;
    // TrackingID
    public TrackingId: number;
    // Solicitado
    public IssuedAt: number;
    // canal de solicitud
    public Issuer: string;
    // no antes de esta fecha:
    public notBefore: number;
    // id del usuario:
    public Subject: string;

    public saveData(storageKey: string): void {
      sessionStorage.setItem(storageKey, JSON.stringify(this));
    }
}
