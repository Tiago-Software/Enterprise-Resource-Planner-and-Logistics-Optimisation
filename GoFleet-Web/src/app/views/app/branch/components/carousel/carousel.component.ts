import { Component, OnInit } from '@angular/core';
import { carouselData, ICarouselItem } from 'src/app/data/carousels';

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html'
})
export class CarouselComponent implements OnInit {
  carouselItems: ICarouselItem[] = carouselData;

  ngOnInit() {
  }

}
