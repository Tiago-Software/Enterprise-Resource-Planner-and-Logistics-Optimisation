import { Component, OnInit } from '@angular/core';
import { Lightbox } from 'ngx-lightbox';

@Component({
  selector: 'app-profile-photos',
  templateUrl: './profile-photos.component.html'
})
export class ProfilePhotosComponent implements OnInit {
  album = [
    {
      src: '/assets/img/marble-cake.jpg',
      thumb: '/assets/img/marble-cake-thumb.jpg'
    },
    {
      src: '/assets/img/parkin.jpg',
      thumb: '/assets/img/parkin-thumb.jpg'
    },
    {
      src: '/assets/img/fruitcake.jpg',
      thumb: '/assets/img/fruitcake-thumb.jpg'
    },
    {
      src: '/assets/img/tea-loaf.jpg',
      thumb: '/assets/img/tea-loaf-thumb.jpg'
    },
    {
      src: '/assets/img/napoleonshat.jpg',
      thumb: '/assets/img/napoleonshat-thumb.jpg'
    },
    {
      src: '/assets/img/magdalena.jpg',
      thumb: '/assets/img/magdalena-thumb.jpg'
    }
  ];
  constructor(private lightbox: Lightbox) {
  }

  openLightbox(index: number): void {
    this.lightbox.open(this.album, index, {centerVertically: true, positionFromTop: 0, disableScrolling: true, wrapAround: true});
  }

  close(): void {
    this.lightbox.close();
  }


  ngOnInit() {
  }

}
