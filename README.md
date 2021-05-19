## HMS-Learning-App

## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](# Sample Code)
 * [License](#license)
 
 
## Introduction
    The sample code is used to implement the function of playing videos through the Video Kit player SDK. 
    The following describes the structure of the sample code:

	activities: UI, which contains the courses related information.
	adapters  : Adapter of the different screens.
	database  : It consist of classes related to database tables and database helper classes.
	dialogs   : A class used to show dialogs.
	fragments : Consists of multiple fragments that are used in different activates.
	models    : Data model classes.
	utils     : A tool class.

## Installation
    To use functions provided by examples, please make sure Huawei Mobile Service 5.0 has been installed on your cellphone. 
    There are two ways to install the sample demo:

    You can compile and build the codes in Android Studio. After building the APK, you can install it on the phone and debug it.
    Generate the APK file from Gradle. Use the ADB tool to install the APK on the phone and debug it adb install 
    {YourPath}\app\release\app-release.apk
    
## Supported Environments
	Android Studio 3.X, JDK 1.8 and later , SDK Platform 19 and later, Gradle 4.6 and later

	
## Configuration 
    Create an app in AppGallery Connect and obtain the project configuration file agconnect-services.json. 
    In Android Studio, switch to the Project view and move the agconnect-services.json file to the root directory of the app.

    Change the value of applicationId in the build.gradle file of the app to the name of the app package applied for in the preceding step.
	
## Sample Code
   
    The HMS Learning app provides demonstration for following scenarios:

    1. Ads integration and demonstrated in Splash Activity and Play Activity.
    2. Integrated the Cloud DB and initialized in Application class. Query, add and update operations are performed using the cloud DB,
       In the app whole data is fetching from Cloud DB.
    3. Integrated the Video Kit and initialized in Application class. In Play Activity, the video is played using the cloud Storage video link.
       Wiseplayer video player is used to perform, play, pause, stop and resume functions are used. 
    4. Cloud Storage is used to store the videos and fetching the videos from cloud storage in application.
    5. Before playing the video, interstitial ad is displayed.
    6. Integrated few common kits like Analytics, Crash Service, APM and Push kit. 
       
##  License
    HMS Learning App sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).