import {Component, Input, OnInit, signal, WritableSignal} from '@angular/core';
import {CommentData} from "../../../search/contracts/interfaces";
import {DestroyableComponent} from "../destroyable/destroyable.component";
import {FormsModule} from "@angular/forms";
import {SearchFacade} from "../../../search/+state/facade/search.facade";
import {debounceTime, Subject} from "rxjs";

@Component({
  selector: 'eventer-comment-section',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.css'
})
export class CommentSectionComponent extends DestroyableComponent implements OnInit {
  @Input({required: true})
  comments: CommentData[] = []

  @Input({required: true})
  eventId: number = -1;

  newCommentValue: string  = ""

  currentUsername: WritableSignal<string> = signal('');

  private debouncedInput$ = new Subject<{ id: number, value: string }>();

  constructor(private readonly searchFacade: SearchFacade) {
    super();
  }

  handleAddCommentClick(){
    this.searchFacade.createComment(this.newCommentValue, this.eventId);
  }

  deleteComment(id:  number) {
    this.searchFacade.deleteComment(id);
  }

  onTextInput(id: number, event: any): void {
    this.debouncedInput$.next({ id, value: event.target.value });
  }

  onCommentChange(id: number, value: string): void {
    this.searchFacade.updateComment(value, id);
  }

  ngOnInit() {
    this.currentUsername.set(localStorage.getItem('username') ?? '');

    this.debouncedInput$.pipe(
      debounceTime(1000)
    ).subscribe(({ id, value }) => {
      this.onCommentChange(id, value);
    });
  }
}
