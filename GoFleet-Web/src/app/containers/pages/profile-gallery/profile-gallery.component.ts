import { Component, OnInit } from '@angular/core';
import { Lightbox } from 'ngx-lightbox';

@Component({
  selector: 'app-profile-gallery',
  templateUrl: './profile-gallery.component.html'
})
export class ProfileGalleryComponent implements OnInit {

  gallerySmall = [
    {
      src: 'img/tea-loaf.jpg',
      thumb: '/assets/img/tea-loaf-thumb.jpg',
    },
    {
      src: '/assets/img/magdalena.jpg',
      thumb: '/assets/img/magdalena-thumb.jpg',
    },
    {
      src: '/assets/img/marble-cake.jpg',
      thumb: '/assets/img/marble-cake-thumb.jpg',
    },
    {
      src: '/assets/img/parkin.jpg',
      thumb: '/assets/img/parkin-thumb.jpg',
    },
    {
      src: '/assets/img/napoleonshat.jpg',
      thumb: '/assets/img/napoleonshat-thumb.jpg',
    },
    {
      src: '/assets/img/fruitcake.jpg',
      thumb: '/assets/img/fruitcake-thumb.jpg',
    },
  ];

  galleryXlarge = [
    {
      src: '/assets/img/fruitcake.jpg',
      thumb: '/assets/img/fruitcake.jpg',
    },
    {
      src: '/assets/img/marble-cake.jpg',
      thumb: '/assets/img/marble-cake.jpg',
    }
  ];

  galleryLarge = [
    {
      src: '/assets/img/parkin.jpg',
      thumb: '/assets/img/parkin-thumb.jpg',
    },
    {
      src: '/assets/img/magdalena.jpg',
      thumb: '/assets/img/magdalena-thumb.jpg',
    },
    {
      src: '/assets/img/napoleonshat.jpg',
      thumb: '/assets/img/napoleonshat-thumb.jpg',
    },
    {
      src: '/assets/img/marble-cake.jpg',
      thumb: '/assets/img/marble-cake-thumb.jpg',
    }
  ];

  constructor(private lightbox: Lightbox) { }

  ngOnInit() {
  }

  openLightbox(gallery, index: number): void {
    this.lightbox.open(gallery, index, { centerVertically: true, positionFromTop: 0, disableScrolling: true, wrapAround: true });
  }

}
