import { Component, OnInit, OnDestroy, Renderer2 } from '@angular/core';
import { FormControl, FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit, OnDestroy {

  //homeForm: FormGroup 
  constructor(private router: Router, private renderer: Renderer2, private fb: FormBuilder) 
  { 
   /* this.homeForm = this.fb.group({
      dob: ['', Validators.required]
    });*/
  }

  loginbutton()
  {
    console.log('login pressed'); 
    this.router.navigate(['/user/login']);
  }


  ngOnInit() {
    //this.renderer.addClass(document.body, 'background');
    //this.renderer.addClass(document.body, 'no-footer');
  }

  ngOnDestroy() {
   // this.renderer.removeClass(document.body, 'background');
   // this.renderer.removeClass(document.body, 'no-footer');
  }
}
