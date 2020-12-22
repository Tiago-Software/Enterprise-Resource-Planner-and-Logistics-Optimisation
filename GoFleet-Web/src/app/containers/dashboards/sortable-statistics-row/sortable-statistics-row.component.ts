import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sortable-statistics-row',
  templateUrl: './sortable-statistics-row.component.html'
})
export class SortableStatisticsRowComponent implements OnInit {

  constructor() { }
  itemsRow = [1, 2, 3, 4];

  ngOnInit() {
  }

}
