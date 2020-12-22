// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  apiUrl: 'https://fa0b9b9b2b67.eu.ngrok.io/',
  googleApi: 'AIzaSyA6li206J2GgrnxbjSJ5ypv5kWQa7DyxIA',//'https://gofleetapi.azurewebsites.net/',
  defaultMenuType: 'menu-default',
  subHiddenBreakpoint: 1440,
  menuHiddenBreakpoint: 768,
  themeColorStorageKey: 'gofleet-themecolor',
  isMultiColorActive: true,
  firebaseConfig : {
    apiKey: "AIzaSyA6li206J2GgrnxbjSJ5ypv5kWQa7DyxIA",
    authDomain: "gofleet-angular.firebaseapp.com",
    databaseURL: "https://gofleet-angular.firebaseio.com",
    projectId: "gofleet-angular",
    storageBucket: "gofleet-angular.appspot.com",
    messagingSenderId: "401335514555",
    appId: "1:401335514555:web:4336fcb9bd06373c95409e",
    measurementId: "G-QRWL9P9NRB"
  },  
  /*
  Color Options:
  'light.blueyale', 'light.blueolympic', 'light.bluenavy', 'light.greenmoss', 'light.greenlime', 'light.yellowgranola', 'light.greysteel', 'light.orangecarrot', 'light.redruby', 'light.purplemonster'
  'dark.blueyale', 'dark.blueolympic', 'dark.bluenavy', 'dark.greenmoss', 'dark.greenlime', 'dark.yellowgranola', 'dark.greysteel', 'dark.orangecarrot', 'dark.redruby', 'dark.purplemonster'
  */
  defaultColor: 'light.orangecarrot',
  isDarkSwitchActive: true,
  defaultDirection: 'ltr',
  themeRadiusStorageKey: 'gofleet-themeradius',
  isAuthGuardActive: true
};
