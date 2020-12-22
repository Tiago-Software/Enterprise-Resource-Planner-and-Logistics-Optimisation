import { Component, OnInit, AfterContentInit, AfterViewInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html'
})
export class AlertsComponent implements OnInit {

  constructor(private translate: TranslateService) { }

  ngOnInit() {

  }
}
