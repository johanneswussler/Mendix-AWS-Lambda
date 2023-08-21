# Lab 3. Integrating AWS Lambda Functions into a Mendix App

## Overview

Welcome to the Mendix AWS Lambda lab! 

This lab has been designed to help you get started with using Mendix with [Amazon Lambda](https://aws.amazon.com/lambda/). After completing this lab, you will have the knowledge of how to use the [Mendix AWS Lambda connector](https://marketplace.mendix.com/link/component/204511) in order to call AWS Lambda functions by first converting an input object to JSON format, and then converting the output JSON of the AWS Lambda function back into a Mendix object.

You will be using the following key services:

* [Amazon Lambda](https://aws.amazon.com/lambda/)


<b>This workshop assumes that you already have some knowledge of AWS, an AWS account, and a Mendix account</b>

You can signup for a free Mendix account for free here: [Sign Up for Free](https://signup.mendix.com/link/signup/?source=none&medium=aws-demo)

You can open an AWS Account and access AWS Free Tier Offers: [Learn more and Create a Free Account](https://aws.amazon.com/free/?all-free-tier&all-free-tier.sort-by=item.additionalFields.SortRank&all-free-tier.sort-order=asc&awsf.Free%20Tier%20Types=*all&awsf.Free%20Tier%20Categories=*all)

## Workshop Outline:

- [Lab 3. Integrating AWS Lambda Functions in a Mendix App](#lab3.-integrating-aws-lambda-into-a-mendix-app)
- [Workshop Outline:](#workshop-outline)
- [1. Create the Amazon Lambda Functions](#1.-create-the-amazon-lambda-functions)   
- [2. Create the Mendix Application](#2.-create-the-mendix-application)
  - [Start Mendix Studio Pro](#start-mendix-studio-pro)
- [3. Import the Connectors](#3.-import-the-connectors)
  - [Add the AWS Authentication Connector](#add-the-aws-authentication-connector)
  - [Add the AWS Lambda Connector](#add-the-aws-lambda-connector)
- [4. Configure the Credentials](#4.-configure-the-credentials)
  - [Static Credentials](#static-credentials)
    - [Provide Permissions to the IAM User](#provide-permissions-to-the-iam-user)
    - [Configure Static Credentials in the Mendix App](#configure-static-credentials-in-the-mendix-app)
  - [Session Credentials](#session-credentials)
    - [Configure Session Credentials in the Mendix App](#configure-session-credentials-in-the-mendix-app)
- [5. Create the User Interface](#5.-create-the-user-interface)
  - [Rename the Module](#rename-the-module)
  - [Create the Domain Model](#create-the-domain-model)
  - [Create the Home Page](#create-the-home-page)
- [6. Create the Application Logic](#6.-create-the-application-logic)

## 1. Create the Amazon Lambda Functions

To start off the lab you need to create two Amazon Lambda functions (getParcelStatus and getParcelLocation) that will later be invoked in the Mendix app.
In both cases, use a Node.js 18.x runtime. Create the functions in a region of your choosing. 
The code for both functions can be copied from below:

Function *getParcelStatus*:
```node.js
export const handler = async(event) => {
    const lastCharacter = event.id.slice(-1);
    var randomNumber = Math.floor(Math.random() * 100);
    
    if (randomNumber > 50){
        return {
            statusCode: 200,
            body: JSON.stringify({Status: 'Delivered'}),
        };
    }
    else {
        return {
            statusCode: 200,
            body: JSON.stringify({Status: 'In Transit'}),
        };
    }
};
```

Function *getParcelLocation*:
```node.js
export const handler = async(event) => {
    const lastCharacter = event.id.slice(-1);
    
    if (lastCharacter == '1'){
        return {
            statusCode: 200,
            body: JSON.stringify({Location: 'Den Haag'}),
        };
    }
    else if(lastCharacter == '2'){
        return {
            statusCode: 200,
            body: JSON.stringify({Location: 'Amsterdam'}),
        };
    }
    else {
        return {
            statusCode: 200,
            body: JSON.stringify({Location: 'Rotterdam'}),
        };
    }
};
```

## 2. Create the Mendix Application

In this lab, you will create your Mendix application from scratch. You can start with any version equal or higher to version 9.18.0. We will use the latest release of the LTS version. At the time of the lab's creation that is version 9.24.5.

### Start Mendix Studio Pro

To create your application start a Mendix Studio Pro. Even if you have already a running instance of Studio Pro running you can create a new application by starting Studio Pro again.

Before launching Mendix Studio Pro make sure you [set your default browser to Firefox or Google Chrome](https://catalog.us-east-1.prod.workshops.aws/workshops/4a8553ab-4dc9-45f8-8f2f-fa417f049653/en-US/02-prereq/1-studio-on-ec2#set-your-default-browser).
1. Launch **Mendix Studio Pro** by double-clicking the icon on the desktop or via the start menu.
2. When you are prompted with **Please sign in**, choose **Go to web sign in** to sign in with your Mendix account credentials.
3. After you have been signed in, switch back to Studio Pro. Under Studio Pro, choose **Create New App**.
4. Under **Choose your starting point**, choose **Blank Web App**, and then choose **Use this starting point**.
5. In the following **App Settings** popup enter *AWSLabs_Lambda* as **App name**, leave the remaining defaults as they are, and choose **Create app**.

After a short moment, the blank app shows up in Studio Pro and the development can commence.

![The initialized blank Mendix app as starting point for the lab](/readme-img/SP_BlankAppStart.png)

## 3. Import the Connectors

In order to enable **Mendix Makers** to easily integrate **AWS services** into their Mendix apps, Mendix has built a suite of connectors that are generally available in the [Mendix Marketplace](https://marketplace.mendix.com/).
In this lab, you will make use of the two connectors
* [AWS Authentication Connector](https://marketplace.mendix.com/link/component/120333). This one is a prerequisite for the use of all the other connectors.
* [AWS Lambda Connector](https://marketplace.mendix.com/link/component/204511). This will enable you to actually invoke Amazon Lambda functions from inside the Mendix app.

### Add the AWS Authentication Connector

1. Click the shopping cart icon next to your Mendix profile picture in the right top corner of the Studio Pro window.
   ![Marketplace button](/readme-img/SP_ShoppingCart.png)
   This will open the Mendix Marketplace in a new popup window.
2. Enter *AWS Authentication Connector* in the search field and press enter.
3. Now all relevant results are shown to you. Click on the AWS Authentication Connector as shown in the image below.
   ![Search results for AWS Authentication Connector in the Mendix marketplace](/readme-img/SP_SearchResultsAuthentication.png)
4. Now the marketplace listing for the Authentication connector opens in the main Studio Pro window. Click on the **Download** button to download the connector into your app. In the following popup dialogue keep the default values and click **Import**. Click **OK** to dismiss the information popup.

### Add the AWS Lambda Connector

To download Mendix' *AWS Lambda Connector* follow the same steps as for the Authentication connector. As a search term use *AWS Lambda Connector* and use the result as shown in the image below.

![Search results for AWS Lambda Connector in the Mendix marketplace](/readme-img/SP_SearchResultsLambda.png).

You can click **Continue** in case of a warning popup about overwriting files.

### Check the Downloads

As a quick check before the next section, verify whether the downloads have been successful. You can do this by expanding the module **App AWSLabs_LaMbda** and then the folder **Marketplace modules** in the **App Explorer** on the left side. You should be able to find both new modules similar to the image below.

![App Explorer with downloaded connectors](/readme-img/SP_AppExplorerAfterDownloads.png)

## 4. Configure the Credentials

For the AWS Lambda Connector to be able to access the Amazon Lambda API, AWS credentials need to be provided inside the Mendix app.

In this lab, you configure credentials to run the AWS connector locally in Mendix Studio Pro. To configure credentials you create a new app configuration with the credentials settings.

You can use [session credentials](https://aws.amazon.com/blogs/security/extend-aws-iam-roles-to-workloads-outside-of-aws-with-iam-roles-anywhere/) or [static credentials](https://docs.aws.amazon.com/general/latest/gr/root-vs-iam.html). In general session credentials are preferred over static credentials because you don't have to embed long-term credentials in your application and you can provide access to AWS services without having to define an AWS identity.

> [!IMPORTANT]
> The Mendix [free environment](https://www.mendix.com/pricing/) supports **session credentials** only when you run your app **locally** in Mendix Studio Pro. When you publish your app use session credentials you must use a > licensed environment.

### Static Credentials

#### Provide Permissions to the IAM User

First of all, the IAM user which was created during the steps in the prerequisites needs to be provided with the permissions to access the Amazon Lambda API.
This can be done by adding a new policy to the user **mendix-workshop-user**:
1. Open [AWS CloudShell](https://console.aws.amazon.com/cloudshell/) or switch to an already open CloudShell tab or window.
2. Use the [put-user-policy](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/iam/put-user-policy.html) and the command below to add an inline policy document that is embedded in the **mendix-workshop-user**.
   ```
   aws iam put-user-policy --user-name mendix-workshop-user --policy-name LambdaInlinePolicy --policy-document '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Action":["lambda:*"],"Resource":"*"}]}'
   ```
3. This command does not return an output.

To retrieve the specified inline policy document that is embedded in the **mendix-workshop-user**, use the following command:

```
aws iam get-user-policy --user-name mendix-workshop-user --policy-name LambdaInlinePolicy
```

#### Configure Static Credentials in the Mendix App

Now that the user **mendix-workshop-user** has been provided with the necessary permissions, the static credentials can be set inside the Mendix app:

1. In the **App Explorer** on the left side, expand **App AWSLabs_Lambda** and then double-click **Settings**.
2. In the **App settings** window under Configuration, choose **New**.
3. Under **Name**, enter *AWSStaticCredentialsConfiguration*.
4. On the **Constants** tab click **New**.
5. Enter *Key* into the search field.
6. Under **Marketplace modules** -> **AmazonLambdaConnector** -> **ConnectionDetails** -> **StaticCredentials**, double-click **AccessKeyID**.
   
   ![Search results for keyword *key* in constant search](/readme-img/SP_ConstantSearchKey.png)
   
7. In the **New Constant Value** popup, under Value, enter (copy/paste) your AWS access key, and then choose **OK**.
8. Add the secret access key in the same manner.
9. To configure the use of static credentials add another constant. Choose **New** and search for *UseSessionBasedCredentials*, and then double-click **UseSessionBasedCredentials**.
10. Under **New Constant Value** set the **Value** to **False**.
11. Under Edit Configuration, choose OK.

    ![New configuration](/readme-img/SP_NewConfig.png)
    
13. In the **App Settings** window, choose **AWSStaticCredentialsConfiguration**, then choose **Make active**, and then click **OK**.

    ![Ready settings](/readme-img/SP_ReadySettings.png)

### Session Credentials

To configure session credentials you provide the **ClientCertificateID**, **ProfileARN**, **RoleARN**, **SessionName**, **TrustAnchorARN**, **ClientCertificates** and **ClientCertificatePasswords**.

Upload the **pfx** file with your certificate that you created earlier to your machine where you use **Mendix Studio Pro**. You can use one of the following methods to upload the certificate.

* When you use Mendix Studio Pro on Amazon EC2 you can redirect a local folder in the remote desktop client to your EC2 instance. You find the folder redirection configuration in the remote desktop client when you edit your connection details under **folder**.
* Use an Amazon S3 bucket. Upload your certificate to a bucket. On your EC2 instance, you can use the command prompt to copy the certificate from your S3 bucket. In the command prompt, you use a command similar to

  ```
  aws s3 cp s3://REPLACE_WITH_YOUR_S3_BUCKET/mx-aws-workshop.pfx
  ```
  
* Use a webmail service. Send the certificate as a mail attachment and access your webmail application with the browser on the EC2 instance.

#### Configure Session Credentials in the Mendix App

For the session credentials configuration, you provide values from your **IAM Roles Anywhere** setup like the **ProfileARN**, **TrustAnchorARN**, and more.

To configure session credentials:

1. In the **App Explorer** on the left side, expand **App AWSLabs_Lambda** and then double-click **Settings**.
2. In the **App settings** window under Configuration, choose **New**.
3. Under **Name**, enter *AWSSessionCredentialsConfiguration*.
4. On the **Constants** tab click **New**.
5. Enter *SessionCredentials* into the search field.
6. Under **Marketplace modules** -> **AmazonLambdaConnector** -> **ConnectionDetails** -> **SessionCredentials**, double-click **AccessKeyID** configure the following settings. You configure each setting by double-clicking on it. This action opens a new popup window for you to enter the respective value. Repeat the steps for searching and selecting each constant:
    1. For the **ClientCertificateID** constant, enter the value *1*.
    2. For the **ProfileARN** constant, enter the value of your **PROFILE_ARN**.
    3. For the **RoleARN** constant, enter the value of your **ROLE_ARN**.
    4. For the **SessionName** constant, enter the value *AWS_MX*.
    5. For the **TrustAnchorARN** constant, enter the value of your **TRUST_ANCHOR_ARN**.
    6. For the **UseSessionBasedCredentials** constant, set the value to *True*. To find this setting search for *UseSessionBasedCredentials*.

       ![New session credentials configuration](/readme-img/SP_NewConfigSession.png)

7. Add two custom variables for the local path of the **pfx** file and the password for your certificate file.
     1. On the **Custom** tab click **New** to create the variable
     2. Add one with the **Name** **ClientCertificatePasswords** and the password for your certificate file as **Value**.
     3. Add one with the **Name** **ClientCertificates** and the path to your certificate file as **Value**.

        ![Custom variable in the session credentials config](/readme-img/SP_ConfigSessionCredentialsCustom.png)

8. Close the **Edit Configuration** window by clicking **OK**.
9. In the **App Settings** window, choose **AWSSessionCredentialsConfiguration**, then choose **Make active**, and then click **OK**.

## 5. Create the User Interface

The use case for the app in this lab will be an easy parcel-tracking system. The two lambda functions that allow that are already in place. So to start things off, you need to create a simple user interface via which the user can enter the tracking number of their parcel and receive the status and location of their parcel back.

### Rename the Module

When the app has been initiated there has already a first module been created. However, it is called **MyFirstModule**. In order to keep the app clear and intuitive, rename the module to *LambdaModule*.

To do this execute the following steps:
1. In the **App Explorer** right-click on the module **MyFirstModule** and then click **Rename...**.
2. Enter the new name, in this case, *LambdaModule*, and click **OK**.

### Create the Domain Model

The [domain model](https://docs.mendix.com/refguide/domain-model/) is a model that describes the information (or data) used by your application in an abstract way.
For this app, you will only need two entities. One that holds the parcel's information and one that temporarily stores the information that lambda returns to the Mendix app.

Follow the steps below to create the domain model:
1. In the **App Explorer** expand the **LambdaModule** and double-click on **Domain model**. This opens the module's domain model in a new tab in the **Studio Pro working area**.
2. Right-click somewhere inside the domain model tab and click **Add entity**.
3. Double-click the newly created entity **Entity** to open the **Properties of Entity LambdaModule.Entity** popup. There, change the entity's name to *Parcel*. For **Persistable** select **No** as for this app, it is not necessary to save the data to the database.
4. In the **Attributes** tab click on **New** to create a new attribute. In the **Common** section of the **Add Attribute** popup, change the name to *TrackingNumber*. In the **Type** section, choose the type **String** from the **Type** dropdown menu. Then click **OK**.
5. Create two more attributes with data type **String**. Name them *ParcelLocation* and *ParcelStatus*. Then close the popup with a click on **OK**.
6. No create a second non-persistable entity named *LambdaResponse* with one attribute named *ResultString* of data type **String**.

### Create the Home Page

Now a home page is needed where a user can enter the tracking number of their parcel and receive a message back.

1. Open the page editor for the home page by double-clicking on **Home_Web** in the **App Explorer**. You will see the default home page.
2. There, first change the existing text. Double-click the first text field and the **Edit Text 'text1'** popup will open. There change the **Caption** in the **General** section from *Home* to *Parcel Tracking* and click **OK**.
3. Change the caption of the second text field to *Here you can track your parcels*.
4. Next, add a **Data view** to the second section of the home page by dragging it from the **Data containers** section in the **Toolbox** pane.
5. Now double-click the new **Data view**. In the popup Select **Vertical** as **Form orientation** in the **General** section.
6. In the **Data source** section select **Microflow** as **Type** and then click on **Select**.
7. In the **Select Microflow** popup click on **New** and enter *DS_Parcel_New* as **Name**. Then click **OK**. Also, close the data view edit popup with another click on **OK**.
8. 




## 6. Create the Application Logic


















