<h1 align="left">
    <img alt="Icon" src="app/src/main/res/drawable/favicon.png" align="left" height="40em" width="40em">
    <span>NotABug Mobile</span>
</h1>

This application was intended to be a nice Material Design 3 compliant frontend for the notabug.org website. It was my first attempt to write an application using Kotlin.

## Project's Fate

As it turns out, [notabug.org](https://notabug.org) uses Gogs as their hosting system, and every Gogs website should have a proper API, right? Even tho I am not aware of how to use such API against [notabug.org](htttps://notabug.org), it's clearly certain that using the API directly is much more appropriate and reliable than parsing the contents of web pages (this is how the app currently works). Therefore, I decided to take a break in development of this app, seeing how its code gets more and more cluttered with tangled inheritance involving generics.

Recently I had discovered an application called [GitTouch](https://f-droid.org/repo/io.github.pd4d10.gittouch), the concept of which I really liked. So, maybe some time in the future I will create a similar frontend for several code-hosting platforms at once, using the knowledge I gained and designs I created during the development of NotABug Mobile.

## Implemented Features

<h4 align="center"> Browse anonymously or while logged in, the app will save your credentials if needed</h4>


<div align="center">
    <img src="media/login-screen.png" alt="Login Screen" width="24%">
    <img src="media/login-screen-warning.png" alt="Login Screen - Warning" width="24%"/>
    <img src="media/login-screen-credentials.png" alt="Login Screen - Credentials" width="24%"/>
    <img src="media/login-screen-progress.png" alt="Login Screen - Loading" width="24%">
</div>

<div align="center">
    <img src="media/dialog-credentials.png" alt="Dialog - Credentials Handling" width="24%"/>
    <img src="media/dialog-registration.png" alt="Dialog - Registration" width="24%"/>
    <img src="media/dialog-anonymous-browsing.png" alt="Dialog - Anonymous Browsing" width="24%"/>
    <img src="media/dialog-login-failure.png" alt="Dialog - Login Failure" width="24%"/>
</div>

<h4 align="center">Personalized browsing</h4>

<div align="center">
    <img src="media/personal-loading-repositories.png" alt="Personal Experience - Repositories" width="32%"/>
    <img src="media/personal-mirrors.png" alt="Personal Experience - Mirrors" width="32%"/>
    <img src="media/personal-organizations.png" alt="Personal Experience - Organizations" width="32%"/>
</div>

<h4 align="center">Anonymous browsing</h4>

<div align="center">
    <img src="media/anonymous-code.png" alt="Anonymous Experience - Browsing Code" width="32%"/>
    <img src="media/anonymous-people.png" alt="Anonymous Experience - Browsing Users" width="32%"/>
    <img src="media/anonymous-search.png" alt="Anonymous Browsing - Search" width="32%">
</div>

<h4 align="center">Review individual repositories and mirrors</h4>

<div align="center">
    <img src="media/repository.png" alt="Repository View" width="32%"/>
    <img src="media/mirror-gimp.png" alt="Mirror View - GIMP" width="32%"/>
    <img src="media/mirror-scrolled.png" alt="Mirror View - Minimized Top Section" width="32%"/>
</div>

## Licensing

NotABug Mobile is licensed under [GNU Affero General Public License version 3](license) and provided "as is" with no warranties and no responsibility for possible damage. 

If you are interested in reviving this project, feel free to open a pull request or an issue, describing your thoughts.