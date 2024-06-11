import {Component, Input} from '@angular/core';
import {CommentData} from "../../../search/contracts/interfaces";
import {DestroyableComponent} from "../destroyable/destroyable.component";
import {FormsModule} from "@angular/forms";
import {SearchFacade} from "../../../search/+state/facade/search.facade";

@Component({
  selector: 'eventer-comment-section',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.css'
})
export class CommentSectionComponent extends DestroyableComponent{
  @Input({required: true})
  comments: CommentData[] = []

  @Input({required: true})
  eventId: number = -1;

  newCommentValue: string  = ""

  constructor(private readonly searchFacade: SearchFacade) {
    super();
  }

  handleAddCommentClick(){
    this.searchFacade.createComment(this.newCommentValue, this.eventId);
  }
}
