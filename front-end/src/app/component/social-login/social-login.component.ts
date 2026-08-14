import { Component } from '@angular/core';

@Component({
  selector: 'app-social-login',
  standalone: true,
  imports: [],
  templateUrl: './social-login.component.html',
  styleUrls: ['./social-login.component.css']
})
export class SocialLoginComponent {

  loginGoogle(): void {

    window.location.href =
      'http://localhost:8080/oauth2/authorization/google';

  }

    loginGithub(): void {

    window.location.href =
      'http://localhost:8080/oauth2/authorization/github';

  }

}
