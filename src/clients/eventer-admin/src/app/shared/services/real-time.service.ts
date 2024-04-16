import {Injectable} from "@angular/core";
import {Observable, Subject, takeUntil} from "rxjs";

@Injectable({providedIn: 'root'})
export class RealTimeService {
  private readonly baseUrl: string = "http://localhost:9006";
  private readonly realTimeServiceRoute = 'api/v1/rts';
  private shouldClose$: Subject<void> = new Subject<void>();

  private connection$ = new Observable(observer => {
    const eventSource = new EventSource(`${this.baseUrl}/${this.realTimeServiceRoute}/stream`);

    eventSource.onmessage = event => {
      const messageData: any = JSON.parse(event.data);
      observer.next(messageData);
    };
  });

  constructor() {}

  public subscribeToChanges() {
    return this.connection$.pipe(takeUntil(this.shouldClose$));
  }

  public closeConnection() {
    this.shouldClose$.next();
  }
}
