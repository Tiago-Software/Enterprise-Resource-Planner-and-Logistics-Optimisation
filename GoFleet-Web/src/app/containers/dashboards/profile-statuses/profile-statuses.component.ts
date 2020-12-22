import { Component, OnInit, Input } from '@angular/core';
import profileStatuses, { IProfileStatus } from 'src/app/data/profile-statuses';

@Component({
  selector: 'app-profile-statuses',
  templateUrl: './profile-statuses.component.html'
})
export class ProfileStatusesComponent implements OnInit {

  @Input() class = '';

  data: IProfileStatus[] = profileStatuses;
  constructor() { }

  ngOnInit() {
  }

}
