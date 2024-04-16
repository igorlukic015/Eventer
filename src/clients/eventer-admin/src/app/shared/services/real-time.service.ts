import {Injectable} from "@angular/core";
import {Observable, skipWhile, Subject, takeUntil} from "rxjs";
import {Message} from "../contracts/interfaces";

@Injectable({providedIn: 'root'})
export class RealTimeService {
  private readonly baseUrl: string = "http://localhost:9006";
  private readonly realTimeServiceRoute = 'api/v1/rts';
  private shouldClose$: Subject<void> = new Subject<void>();

  private connection$ = new Observable<Message>(observer => {
    const eventSource = new EventSource(`${this.baseUrl}/${this.realTimeServiceRoute}/stream`);

    eventSource.onmessage = event => {
      const messageData: Message = JSON.parse(event.data);
      observer.next(messageData);
    };
  });

  constructor() {}

  public subscribeToChanges() {
    return this.connection$.pipe(takeUntil(this.shouldClose$), skipWhile(() => {
      const token = localStorage.getItem('token')
      return token === null;
    }));
  }

  public closeConnection() {
    this.shouldClose$.next();
  }
}
