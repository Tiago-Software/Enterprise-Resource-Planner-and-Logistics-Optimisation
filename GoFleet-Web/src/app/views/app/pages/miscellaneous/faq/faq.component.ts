import { Component, OnInit } from '@angular/core';
import faqData, { IFaq } from 'src/app/data/faq';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html'
})
export class FaqComponent implements OnInit {

  data: IFaq[] = faqData;
  showItemIndex = 0;
  constructor() { }

  ngOnInit() {
  }
}
