import {Component} from '@angular/core';
import {EventCategory} from "../../contracts/interfaces";

@Component({
  selector: 'eventer-admin-event-category-list',
  standalone: true,
  imports: [],
  templateUrl: './event-category-list.component.html',
  styleUrl: './event-category-list.component.css'
})
export class EventCategoryListComponent {
  public categories: EventCategory[] = [
    {id: 1, name: "Category 1", description: "Description for category 1"},
    {id: 2, name: "Category 2", description: "Description for category 2"},
    {id: 3, name: "Category 3", description: "Description for category 3"},
    {id: 4, name: "Category 4", description: "Description for category 4"},
    {id: 5, name: "Category 5", description: "Description for category 5"},
    {id: 6, name: "Category 6", description: "Description for category 6"},
    {id: 7, name: "Category 7", description: "Description for category 7"},
    {id: 8, name: "Category 8", description: "Description for category 8"},
    {id: 9, name: "Category 9", description: "Description for category 9"},
    {id: 10, name: "Category 10", description: "Description for category 10"},
    {id: 11, name: "Category 11", description: "Description for category 11"},
    {id: 12, name: "Category 12", description: "Description for category 12"},
    {id: 13, name: "Category 13", description: "Description for category 13"},
    {id: 14, name: "Category 14", description: "Description for category 14"},
    {id: 15, name: "Category 15", description: "Description for category 15"},
    {id: 16, name: "Category 16", description: "Description for category 16"},
    {id: 17, name: "Category 17", description: "Description for category 17"},
    {id: 18, name: "Category 18", description: "Description for category 18"},
    {id: 19, name: "Category 19", description: "Description for category 19"},
    {id: 20, name: "Category 20", description: "Description for category 20"}
  ];
}
