import { Component, OnInit } from '@angular/core';
import products from 'src/app/data/products';
import { IProduct } from 'src/app/data/api.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html'
})
export class SearchComponent implements OnInit {

  data: IProduct[] = products;
  constructor() { }

  ngOnInit() {
  }

}
