# Lab3. Integrating AWS Lambda Functions in a Mendix App

# Overview

Welcome to the Mendix AWS Lambda lab! 

This lab has been designed to help you get started with using Mendix with [Amazon Lambda](https://aws.amazon.com/lambda/). After completing this lab, you will have the knowledge of how to use the [Mendix AWS Lambda connector](https://marketplace.mendix.com/link/component/204511) in order to call AWS Lambda functions by first converting an input object to JSON format, and then converting the output JSON of the AWS Lambda function back into a Mendix object.

You will be using the following key services:

* [Amazon Lambda](https://aws.amazon.com/lambda/)


<b>This workshop assumes that you already have some knowledge of AWS, an AWS account, and a Mendix account</b>

You can signup for a free Mendix account for free here: [Sign Up for Free](https://signup.mendix.com/link/signup/?source=none&medium=aws-demo)

You can open an AWS Account and access AWS Free Tier Offers: [Learn more and Create a Free Account](https://aws.amazon.com/free/?all-free-tier&all-free-tier.sort-by=item.additionalFields.SortRank&all-free-tier.sort-order=asc&awsf.Free%20Tier%20Types=*all&awsf.Free%20Tier%20Categories=*all)

# Workshop Outline:

- [Mendix AWS Rekoginition Template](#mendix-aws-rekoginition-template)
- [Workshop Outline:](#workshop-outline)
  - [AWS Build](#aws-build)
    - [Create the Amazon Lambda Functions](#create-the-amazon-lambda-functions)   
  - [Mendix Build](#mendix-build)

## AWS Build

### Create the Amazon Lambda Functions

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

## Mendix Setup

