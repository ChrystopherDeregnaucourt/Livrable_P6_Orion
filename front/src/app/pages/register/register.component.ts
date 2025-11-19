import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const { username, email, password } = this.registerForm.value;
      
      this.authService.register({ username, email, password }).subscribe({
        next: (response) => {
          console.log('Inscription réussie:', response.user);
          this.router.navigate(['/articles']);
        },
        error: (error) => {
          console.error('Erreur d\'inscription:', error);
          alert('Erreur lors de l\'inscription');
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
