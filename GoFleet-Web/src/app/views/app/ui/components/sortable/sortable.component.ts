import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sortable',
  templateUrl: './sortable.component.html'
})
export class SortableComponent implements OnInit {
  itemsRow = [1, 2, 3, 4];
  itemsBasic = [1, 2, 3, 4];
  itemsHandles = [1, 2, 3, 4];

  itemsUpdates = [1, 2, 3, 4];
  optionsUpdates = {};
  updates = [];

  constructor() {
    this.optionsUpdates = {
      onUpdate: (event: any) => {
        this.updates.push(this.itemsUpdates.slice());
      }
    };
  }

  ngOnInit() {
  }




}
