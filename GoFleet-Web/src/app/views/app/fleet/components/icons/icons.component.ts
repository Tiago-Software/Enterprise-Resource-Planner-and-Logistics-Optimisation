import { Component, OnInit } from '@angular/core';
import { IconsService } from './icons.service';

@Component({
  selector: 'app-icons',
  templateUrl: './icons.component.html'
})
export class IconsComponent implements OnInit {

  simpleLineIcons;
  iconsMind;

  constructor(private iconsService: IconsService) {
    this.simpleLineIcons = iconsService.simplelineicons;
    this.iconsMind = iconsService.iconsmind;
  }

  ngOnInit() {
  }

}
