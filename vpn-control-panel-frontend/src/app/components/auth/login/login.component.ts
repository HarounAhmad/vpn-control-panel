import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../service/auth.service";
import {Router} from "@angular/router";
import {InputText} from "primeng/inputtext";
import {FormsModule} from "@angular/forms";
import {Password} from "primeng/password";
import {ButtonDirective} from "primeng/button";
import {FloatLabel} from "primeng/floatlabel";
import {IftaLabel} from "primeng/iftalabel";
import {AlertsService} from "../../../service/util/alerts.service";

@Component({
  selector: 'app-login',
  imports: [
    InputText,
    FormsModule,
    Password,
    ButtonDirective,
    FloatLabel,
    IftaLabel
  ],
  templateUrl: './login.component.html',
  standalone: true,
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit{
  username: any;
  password: any;

  constructor(
    private authService: AuthService,
    private router: Router,
    private alertService: AlertsService
  ) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/clients']);
    }
  }

  ngOnInit() {

  }


  login() {
    if (this.loginDisabled()) return;
    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.authService.loadUserInfo().subscribe({
          next: () => {
            this.router.navigate(['/clients']);
            this.alertService.clear();
            },
          error: () => {
            this.alertService.errorSelfClosing('Login Failed', 'Failed to load user information')
          }
        });
      },
      error: () => {
        this.alertService.errorSelfClosing('Login Failed ', 'Invalid username or password');
      }
    });
  }

  loginDisabled() {
    return !this.username || !this.password;
  }
}
