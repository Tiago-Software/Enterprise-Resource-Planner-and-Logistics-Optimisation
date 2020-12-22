import { ForgotPasswordComponent } from './../forgot-password/forgot-password.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  emp_role: string;
  emp_Log_Email: string;
  emp_Log_Password: string;

  buttonDisabled = false;
  buttonState = '';

  constructor(private fb: FormBuilder,private authService: AuthService, private router: Router) 
  {
    this.loginForm= this.fb.group({
      'emp_Log_Email': ['',[Validators.required]],
      'emp_Log_Password': ['',[Validators.required]]
    })
  }

  ngOnInit(): void {
  }

  login() {
    this.authService.login(this.loginForm.value).subscribe(data=> {
      this.authService.saveToken(data['token']);
      this.authService.saveUser(data['emp_First_Name'], data['emp_Last_Name'], data['emp_Id']);
      this.authService.saveUserRole(data['emp_Role']);

      console.log(data)
      console.log(data['emp_Id'])
      this.emp_role = data['emp_Role']
      this.emp_Log_Email = data['emp_Log_Email']

      if(this.emp_role == "Manager")
      {
        this.router.navigate(['/app'])
        console.log(data['emp_Role'])
      }else if(this.emp_role == "Consultant")
      {
        this.router.navigate(['/app'])
        console.log(data['emp_Role'])
      }
  },
  err=> console.log(err))
}
get email()
{
  return this.loginForm.get('emp_Log_Email')
}
get password()
{
  return this.loginForm.get('emp_Log_Password')
}
}
