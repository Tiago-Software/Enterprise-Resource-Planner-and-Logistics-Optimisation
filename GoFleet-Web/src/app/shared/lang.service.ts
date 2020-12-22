import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

import en from '../../lang/en-US.json';
import es from '../../lang/es-ES.json';
import { Router } from '@angular/router';

const languageKey = '__lang';

@Injectable({
  providedIn: 'root',
})
export class LangService {
  isSingleLang = false;
  renderer: Renderer2;
  defaultLanguage = localStorage.getItem(languageKey) || 'en-US';
  supportedLanguages: Language[] = [
    { code: 'en-US', direction: 'ltr', label: 'English', shorthand: 'en' },
    { code: 'es-ES', direction: 'ltr', label: 'isiZulu', shorthand: 'es' },
    { code: 'en-EN', direction: 'rtl', label: 'English - RTL', shorthand: 'enrtl' }
  ];

  constructor(private translate: TranslateService, private rendererFactory: RendererFactory2, private router: Router) {
    this.renderer = this.rendererFactory.createRenderer(null, null);
  }

  init() {
    this.translate.setTranslation('en-US', en);
    this.translate.setTranslation('es-ES', es);
    this.translate.setTranslation('en-EN', en);
    this.translate.setDefaultLang(this.defaultLanguage);
    if (this.isSingleLang) {
      this.translate.use(this.defaultLanguage);
    } else {
      this.language = '';
    }
  }

  checkForDirectionChange() {
    this.renderer.removeClass(document.body, 'ltr');
    this.renderer.removeClass(document.body, 'rtl');
    this.renderer.addClass(document.body, this.direction);
    this.renderer.setAttribute(document.documentElement, 'direction', this.direction);
  }

  set language(lang: string) {
    let language = lang || localStorage.getItem(languageKey);
    const isSupportedLanguage = this.supportedLanguages.map(item => item.code).includes(language);
    if (!isSupportedLanguage) {
      language = this.defaultLanguage;
    }

    if (lang !== '' && this.supportedLanguages.map(item => item.code).includes(lang) && this.direction !== this.supportedLanguages.find(item => item.code === lang).direction) {
      localStorage.setItem(languageKey, lang);
      window.location.reload();
    } else {
      this.translate.use(language);
    }
    this.checkForDirectionChange();
    localStorage.setItem(languageKey, language)
  }

  get language(): string {
    return this.translate.currentLang;
  }

  get languageShorthand(): string {
    return this.supportedLanguages.find(item => item.code === this.translate.currentLang).shorthand;
  }

  get direction(): string {
    return this.supportedLanguages.find(item => item.code === this.translate.currentLang).direction;
  }

  get languageLabel(): string {
    return this.supportedLanguages.find(item => item.code === this.translate.currentLang).label;
  }
}

export class Language {
  code: string;
  direction: string;
  label: string;
  shorthand: string;
}
