import { Component, OnInit } from '@angular/core';
import { Router, Event, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'app-heading',
  templateUrl: './heading.component.html'
})
export class HeadingComponent {
  currentRoute = '';

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    this.router.events
    .pipe(
      filter((event) => event instanceof NavigationEnd),
      map(() => this.activatedRoute),
      map((route) => {
        while (route.firstChild) { route = route.firstChild; }
        return route;
      })
    ).subscribe((event) => {
      const path = this.router.url.split('?')[0];
      const paramtersLen = Object.keys(event.snapshot.params).length;
      const pathArr = path.split('/').slice(0, path.split('/').length - paramtersLen);
      this.currentRoute = pathArr[pathArr.length - 1];
    });
  }
}
