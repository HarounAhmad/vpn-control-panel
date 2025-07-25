import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../service/auth.service";
import {Router} from "@angular/router";
import {InputText} from "primeng/inputtext";
import {FormsModule} from "@angular/forms";
import {Password} from "primeng/password";
import {ButtonDirective} from "primeng/button";
import {Toast} from "primeng/toast";
import {MessageService} from "primeng/api";
import {FloatLabel} from "primeng/floatlabel";
import {IftaLabel} from "primeng/iftalabel";

@Component({
  selector: 'app-login',
  imports: [
    InputText,
    FormsModule,
    Password,
    ButtonDirective,
    Toast,
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
    private messageService: MessageService
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
            },
          error: () => {
            this.messageService.add({severity: 'error', summary: 'Login Failed', detail: 'Failed to load user information'});
          }
        });
      },
      error: () => {
        this.messageService.add({severity: 'error', summary: 'Login Failed', detail: 'Invalid username or password'});
      }
    });
  }

  loginDisabled() {
    return !this.username || !this.password;
  }
}
