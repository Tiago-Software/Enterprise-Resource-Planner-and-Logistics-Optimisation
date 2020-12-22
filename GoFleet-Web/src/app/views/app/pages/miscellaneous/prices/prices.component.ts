import { Component, OnInit } from '@angular/core';
import priceData, { IPrice } from 'src/app/data/prices';
import { LangService } from 'src/app/shared/lang.service';

@Component({
  selector: 'app-prices',
  templateUrl: './prices.component.html'
})
export class PricesComponent implements OnInit {

  locale = '';

  data: IPrice = priceData;

  constructor(private langService: LangService) {
    this.locale = this.langService.languageShorthand;
   }

  ngOnInit() {
  }

}
