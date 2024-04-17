import {Component, EventEmitter, Input, Output, signal, WritableSignal} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {SelectListElement} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-admin-select-list',
  standalone: true,
    imports: [
        ReactiveFormsModule
    ],
  templateUrl: './select-list.component.html',
  styleUrl: './select-list.component.css'
})
export class SelectListComponent {
  @Input({required: true})
  title: string = 'Click to select';

  @Input({required: true})
  placeholder: string = 'Select';

  @Input({required: true})
  data: SelectListElement[] = [];

  @Output()
  changeElement: EventEmitter<SelectListElement[]> = new EventEmitter();

  isViewChecked: WritableSignal<boolean> = signal(false);
  selectedData: WritableSignal<SelectListElement[]> = signal([]);

  onViewChange($event: any) {
    this.isViewChecked.set(!this.isViewChecked());
  }

  onSelectChange($event: any) {
    const foundElement =
      this.data.find((e) => e.id == $event.target.value);

    if(!foundElement) {
      return;
    }

    const updatedList = [...this.selectedData(), foundElement];

    this.selectedData.set(updatedList)
    this.changeElement.emit(updatedList);
  }

  onRemoveSelectedElement($event: any, selectedElementId: number) {
    const updatedList = this.selectedData().filter((e) => e.id !== selectedElementId);
    this.selectedData.set(updatedList);
    this.changeElement.emit(updatedList);
  }
}
