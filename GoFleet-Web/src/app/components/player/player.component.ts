import { Component, OnInit, ViewChild, ElementRef, AfterContentInit, Input, OnDestroy } from '@angular/core';
import videojs from 'video.js';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html'
})
export class PlayerComponent implements OnInit, AfterContentInit, OnDestroy {
  @ViewChild('videoPlayer', { static: true }) videoPlayer: ElementRef;
  @Input() options = {};
  player;
  constructor() { }

  ngAfterContentInit() {
    this.player = videojs(this.videoPlayer.nativeElement, this.options, function onPlayerReady() {
    });
  }

  ngOnDestroy() {
    if (this.player) {
      this.player.dispose();
    }
  }

  ngOnInit() {
  }

}
