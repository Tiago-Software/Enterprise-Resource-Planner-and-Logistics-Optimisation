import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AuthService } from 'src/app/shared/auth.service';

@Component({
  selector: 'app-views',
  templateUrl: './views.component.html'
})
export class ViewsComponent implements OnInit {

  constructor( private authService: AuthService,private router: Router) {
    // If you have landing page, remove below line and implement it here.
    if(localStorage.getItem('token') == authService.getToken() && localStorage.getItem('token') != null)
    {
       this.router.navigateByUrl('/app');
    }else
    {
      this.router.navigateByUrl('/user');
    }

  }

  ngOnInit() {

  }

}
