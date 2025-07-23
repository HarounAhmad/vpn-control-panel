import {Component, Input} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {Location} from "@angular/common";
import {Button} from "primeng/button";
import {IconField} from "primeng/iconfield";
import {InputIcon} from "primeng/inputicon";
import {InputText} from "primeng/inputtext";
import {Toolbar} from "primeng/toolbar";

@Component({
  selector: 'app-title-bar',
  imports: [
    Button,
    IconField,
    InputIcon,
    InputText,
    Toolbar,
    RouterLink
  ],
  templateUrl: './title-bar.component.html',
  standalone: true,
  styleUrl: './title-bar.component.scss'
})
export class TitleBarComponent {

  @Input() title: string = '';

  constructor(
    private location: Location
  ) {

  }

  goBack() {
    this.location.back()
  }

}
