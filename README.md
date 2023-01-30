# bookish-couscous (Github Sample Client For bitFlyer Japan)

## Instructions
 - Build the app using Android Studio Electric Eel | 2022.1.1
 - You can run the app as is and it should work fine
 - You can also add your GH personal token to `app/src/main/java/com/doivid/githubclient/api/AuthInterceptor.kt` in order to increase the number of requests you can made before the Github API starts error-ing out with 403 http response codes

## Missing parts
 - No proper layouts for error and loading states
 - No proper layout/parsing for the user events
 - No way of authenticating the user from the app
 - No proper theming/color setup

## Possible improvements
 - Better specify the architecture, my implementation was loosely MVVM
 - For the github api, I think it might've been better to use their graphql api to make the content more meaningful and make the app feel more populated (the /users api doesn't return the user name and bio for example)
 - It would've been better to have some design specs/ideas