import { Component, OnInit, ViewChild, EventEmitter, Output, Input } from '@angular/core';
import { AuthService } from 'src/app/shared/auth.service';

@Component({
  selector: 'app-list-route-header',
  templateUrl: './list-route-header.component.html'
})
export class ListRouteHeaderComponent implements OnInit {
  
  @Output() addNewItem: EventEmitter<any> = new EventEmitter();

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

optimise()
  {
    this.authService.optimise().subscribe(null,null,()=> {
     window.location.reload();
     });
  }
}
