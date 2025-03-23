import {Directive, OnDestroy} from "@angular/core";
import {Subject} from "rxjs";

@Directive()
export abstract class DestroyableComponent implements OnDestroy {
  protected destroyed$: Subject<void> = new Subject<void>();

  ngOnDestroy() {
    this.destroyed$.next();
    this.destroyed$.complete();
  }
}
