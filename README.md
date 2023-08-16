# Lab3. Integrating AWS Lambda Functions into a Mendix App

# Overview

Welcome to the Mendix AWS Lambda lab! 

This lab has been designed to help you get started with using Mendix with [Amazon Lambda](https://aws.amazon.com/lambda/). After completing this lab, you will have the knowledge of how to use the [Mendix AWS Lambda connector](https://marketplace.mendix.com/link/component/204511) in order to call AWS Lambda functions by first converting an input object to JSON format, and then converting the output JSON of the AWS Lambda function back into a Mendix object.

You will be using the following key services:

* [Amazon Lambda](https://aws.amazon.com/lambda/)


<b>This workshop assumes that you already have some knowledge of AWS, an AWS account, and a Mendix account</b>

You can signup for a free Mendix account for free here: [Sign Up for Free](https://signup.mendix.com/link/signup/?source=none&medium=aws-demo)

You can open an AWS Account and access AWS Free Tier Offers: [Learn more and Create a Free Account](https://aws.amazon.com/free/?all-free-tier&all-free-tier.sort-by=item.additionalFields.SortRank&all-free-tier.sort-order=asc&awsf.Free%20Tier%20Types=*all&awsf.Free%20Tier%20Categories=*all)

# Workshop Outline:

- [Lab 3. Integrating AWS Lambda Functions in a Mendix App](#lab3.-integrating-aws-lambda-into-a-mendix-app)
- [Workshop Outline:](#workshop-outline)
- [1. Create the Amazon Lambda Functions](#1.-create-the-amazon-lambda-functions)   
- [2. Create the Mendix Application](#2.-create-the-mendix-application)
  - [Start Mendix Studio Pro](#start-mendix-studio-pro)

# 1. Create the Amazon Lambda Functions

To start off the lab you need to create two Amazon Lambda functions (getPackageStatus and getPackageLocation) that will later be invoked in the Mendix app.
In both cases, use a Node.js 18.x runtime. Create the functions in a region of your choosing. 
The code for both functions can be copied from below:

Function *getPackageStatus*:
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

Function *getPackageLocation*:
```node.js
export const handler = async(event) => {
    
   
    
    const lastCharacter = event.id.slice(-1);
    
    if (lastCharacter == '1'){
        return {
            statusCode: 200,
            body: JSON.stringify({Stad: 'Den Haag'}),
        };
    }
    else if(lastCharacter == '2'){
        return {
            statusCode: 200,
            body: JSON.stringify({Stad: 'Amsterdam'}),
        };
    }
    else {
        return {
            statusCode: 200,
            body: JSON.stringify({Stad: 'Rotterdam'}),
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


