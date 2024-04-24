import {Component, computed, EventEmitter, Input, OnInit, Output, Signal, signal, WritableSignal} from '@angular/core';
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
export class SelectListComponent implements OnInit {
  @Input({required: true})
  title: string = 'Click to select';

  @Input({required: true})
  placeholder: string = 'Select';

  @Input({required: true})
  allData: SelectListElement[] = [];

  @Input()
  initialData: SelectListElement[] = [];

  @Output()
  changeElement: EventEmitter<SelectListElement[]> = new EventEmitter();

  isViewChecked: WritableSignal<boolean> = signal(false);
  optionData: Signal<SelectListElement[]> = computed(() => {
    return this.allData.filter(e => !this.selectedData().map(s => s.id).includes(e.id));
  });
  selectedData: WritableSignal<SelectListElement[]> = signal([]);

  onViewChange($event: any) {
    this.isViewChecked.set(!this.isViewChecked());
  }

  onSelectChange($event: any) {
    const foundElement =
      this.allData.find((e) => e.id == $event.target.value);

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

  ngOnInit(): void {
    this.selectedData.set(this.initialData);
  }
}
