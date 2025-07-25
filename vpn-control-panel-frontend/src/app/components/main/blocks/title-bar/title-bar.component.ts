import {Component, Input, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {Location, NgIf} from "@angular/common";
import {Button} from "primeng/button";
import {IconField} from "primeng/iconfield";
import {InputIcon} from "primeng/inputicon";
import {InputText} from "primeng/inputtext";
import {Toolbar} from "primeng/toolbar";
import {SplitButton} from "primeng/splitbutton";
import {MenuItem} from "primeng/api";
import {AuthService} from "../../../../service/auth.service";

@Component({
  selector: 'app-title-bar',
  imports: [
    Button,
    IconField,
    InputIcon,
    InputText,
    Toolbar,
    RouterLink,
    SplitButton,
    NgIf
  ],
  templateUrl: './title-bar.component.html',
  standalone: true,
  styleUrl: './title-bar.component.scss'
})
export class TitleBarComponent implements OnInit{

  @Input() title: string = '';
  items: MenuItem[] | undefined;

  username: string = '';

  constructor(
    private location: Location,
    private authService: AuthService,
    private router: Router,
  ) {
    this.items = [
      {
        label: 'Logout',
        icon: 'pi pi-sign-out',
        command: () => this.logout()
      },
      {
        label: 'Settings',
        icon: 'pi pi-cog',
        command: () => this.openSettingsDialog()
      }
    ];



  }

  ngOnInit() {
    console.log("init")
    this.authService.loadUserInfo().subscribe( {
      next: (user) => {
        this.username = user.username;
      },
      error: (err) => {
        console.error('Failed to load user info', err);
      }
    });
  }

  hasRole(role: string): boolean {
    return this.authService.hasRole(role)
  }

  private logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['']);
      },
      error: (err) => {
        console.error('Logout failed', err);
        this.router.navigate(['']);
      }
    });
  }

  private openSettingsDialog() {

  }
}
