import {Component, Input} from '@angular/core';
import {CommentData} from "../../../search/contracts/interfaces";

@Component({
  selector: 'eventer-comment-section',
  standalone: true,
  imports: [],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.css'
})
export class CommentSectionComponent {
  @Input({required: true})
  public comments: CommentData[] = []
}
