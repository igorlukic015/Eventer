import {Component, Input} from '@angular/core';
import {Image} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-image-carousel',
  standalone: true,
  imports: [],
  templateUrl: './image-carousel.component.html',
  styleUrl: './image-carousel.component.css'
})
export class ImageCarouselComponent {
  @Input({required: true})
  public images: Image[] = []
}
