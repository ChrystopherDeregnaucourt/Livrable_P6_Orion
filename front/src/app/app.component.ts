import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: false
})
export class AppComponent implements OnInit {
  title = 'front';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Initialiser l'utilisateur si un token existe
    this.authService.initializeUser();
  }
}
