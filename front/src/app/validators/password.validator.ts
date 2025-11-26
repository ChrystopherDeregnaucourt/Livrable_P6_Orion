import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Validateur personnalisé pour le mot de passe
 * Un mot de passe valide doit :
 * - Contenir au moins 8 caractères
 * - Contenir au moins un chiffre
 * - Contenir au moins une lettre minuscule
 * - Contenir au moins une lettre majuscule
 * - Contenir au moins un caractère spécial
 */
export function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null; // Ne pas valider si le champ est vide (utiliser Validators.required séparément)
    }

    const hasMinLength = value.length >= 8;
    const hasNumber = /[0-9]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasUpperCase = /[A-Z]/.test(value);
    const hasSpecialChar = /[@#$%^&+=!*()_\-{}\[\]:;"'<>,.?/~`|]/.test(value);

    const passwordValid = hasMinLength && hasNumber && hasLowerCase && hasUpperCase && hasSpecialChar;

    if (!passwordValid) {
      return {
        passwordStrength: {
          hasMinLength,
          hasNumber,
          hasLowerCase,
          hasUpperCase,
          hasSpecialChar
        }
      };
    }

    return null;
  };
}
