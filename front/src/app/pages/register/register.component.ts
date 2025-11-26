import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';
import { passwordValidator } from '../../validators/password.validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: false
})
export class RegisterComponent implements OnInit, OnDestroy {
  registerForm!: FormGroup;
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), passwordValidator()]],
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Vérifie si le mot de passe contient au moins un chiffre
  hasNumber(): boolean {
    const password = this.registerForm.get('password')?.value;
    return password ? /[0-9]/.test(password) : true;
  }

  // Vérifie si le mot de passe contient au moins une minuscule
  hasLowerCase(): boolean {
    const password = this.registerForm.get('password')?.value;
    return password ? /[a-z]/.test(password) : true;
  }

  // Vérifie si le mot de passe contient au moins une majuscule
  hasUpperCase(): boolean {
    const password = this.registerForm.get('password')?.value;
    return password ? /[A-Z]/.test(password) : true;
  }

  // Vérifie si le mot de passe contient au moins un caractère spécial
  hasSpecialChar(): boolean {
    const password = this.registerForm.get('password')?.value;
    return password ? /[@#$%^&+=!*()_\-{}\[\]:;"'<>,.?/~`|]/.test(password) : true;
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const { username, email, password } = this.registerForm.value;
      
      this.authService.register({ username, email, password })
        .pipe(takeUntil(this.destroy$))
        .subscribe({
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
