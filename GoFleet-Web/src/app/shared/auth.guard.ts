import { AuthService } from './auth.service';
import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, CanDeactivate, CanLoad, Route, UrlSegment, Router, UrlTree, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { truncate } from 'fs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild, CanDeactivate<unknown>, CanLoad {
  token
  constructor(private authService: AuthService, private router: Router)
  {
    this.token = authService.getToken();
    console.log(authService.getToken());
  }

  canActivate(next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean
  {
    if(localStorage.getItem('token') != null)
    {
      console.log(this.authService.getToken().toString());
      return true;
    }else
    {
      this.router.navigate(['/user']);
      console.log(this.authService.getToken());
      return false;
    }
  }
  
  canActivateChild()
  {
   return true;
  }
  canDeactivate()
  {
    return true;
  }
  canLoad() 
  {
    return true;
  }
}
