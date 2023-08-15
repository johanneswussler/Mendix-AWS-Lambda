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
  - [Mendix Setup](#mendix-setup)
    - [Windows Setup](#windows-setup)
    - [Mac or Non-Windows Setup - Launch windows EC2 Instance with Mendix Studio Pro Installed](#mac-or-non-windows-setup---launch-windows-ec2-instance-with-mendix-studio-pro-installed)
    - [Download release from this repo](#download-release-from-this-repo)
  - [Setup your project](#setup-your-project)
  - [The Mendix Build](#the-mendix-build)
    - [Creating your AWS Keys](#creating-your-aws-keys)
    - [Security Policy for Rekognition](#security-policy-for-rekognition)
    - [Setting your AWS Access and Secret Keys in Mendix](#setting-your-aws-access-and-secret-keys-in-mendix)
    - [Setting up the Rekognition constants](#setting-up-the-rekognition-constants)
    - [Building the domain model](#building-the-domain-model)
    - [Building the User Interface](#building-the-user-interface)
    - [Building the logic](#building-the-logic)
- [Bonus](#bonus)
- [Clean up](#clean-up)
  - [Amazon Rekogniton Clean up](#amazon-rekogniton-clean-up)
  - [Amazon S3 Clean up](#amazon-s3-clean-up)
  - [Amazon EC2 Clean up](#amazon-ec2-clean-up)
- [Frequently Asked Questions (FAQ)](#frequently-asked-questions-faq)
- [Troubleshooting](#troubleshooting)
- [Your feedback](#your-feedback)

## AWS Build

### Create the Amazon Lambda Functions

To start off the lab you need to create two Amazon Lambda functions that will later be invoked in the Mendix app.
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

We will be using Mendix Studio Pro to develop our app, which requires a Windows Operating system. If you don't have Windows installed, follow the below instructions for [Launching a Windows machine on EC2](#mac-or-non-windows-setup---launch-windows-ec2-instance-with-mendix-studio-pro-installed)

### Windows Setup

If you are using a Windows machine, all you will need to do is download & Install Mendix Studio Pro

1. Download the latest Mendix Studio Pro from the [Marketplace](https://marketplace.mendix.com/link/studiopro/)
2. Install this onto your machine (this will require some administrative privileges)

<img src="readme-img/studio-install.gif">

Now this is done, you can jump to [Download release from this repo](#download-release-from-this-repo)

### Mac or Non-Windows Setup - Launch windows EC2 Instance with Mendix Studio Pro Installed

We have published a Cloudformation Template that you can easily launch which is an EC2 Instance with an AMI that contains Mendix Studio Pro already installed. If you have Windows, you can skip to [Download release from this repo](#download-release-from-this-repo)

**This is a temporary solution, Mendix Studio Pro will be released on Mac in the near future**

**IMPORTANT: The region where you spin up the EC2 Instance can be different from where you create your Rekognition model**

1. In order for the Cloudformation process to work you first need to create a EC2 Keypair. This can be created by searching for EC2 in the AWS Console, clicking on keypairs and creating a new one.

<img src="readme-img/create-keypair.png">

2. The Key pair dialog box will open, give it a name and select the PEM option.

<img src="readme-img/keypair-pem.png"/>


3. Click create key pair, which will generate you a key in PEM format and download to your computer. You will need this key pair to successfully run the Cloud Formation process.

4. When you have the Key Pair you can launch the cloud formation template:

[<img src="https://s3.amazonaws.com/cloudformation-examples/cloudformation-launch-stack.png">](https://console.aws.amazon.com/cloudformation/home?region=eu-central-1#/stacks/new?stackName=MendixStudioPro-Windows&templateURL=https://mendix-aws.s3.eu-central-1.amazonaws.com/cf1.json)

5. In the cloudformation steps select the newly created keypair under the keyname option.

<img src="readme-img/cf-step.png"/>

6. Use the default values for all the other steps during the cloud formation process. Once your cloudformation script has complete you'll have a new EC2 instance provisioned with everything you need to build your Mendix app.

7. Once the stack is created open EC2 from the AWS Console. Click on Instances, find your newly created instance, and click on the instance id.

<img src="readme-img/running-instance.png"/>

8. Click on **Connect** in the top right of the instance overview. Select on the option **RDP client**.

<img src="readme-img/connect-via-rdp.png"/>

9. Download the remote desktop file.

10. Next Click on **Get password**. This password you will need to login via RDP to the windows desktop.

<img src="readme-img/get-windows-password.png"/>

11. Upload your pem key pair file that you created earlier and hit **Decrypt Password**. You'll see that this dialog will close and your password will appear on the previous page. Copy the password.

12. Run the RDP file that you downloaded earlier and paste the password into the dialog when prompted.

You should now see your windows desktop with Mendix installed. 

<img src="readme-img/cf2.png"/>

### Download release from this repo

1. Once you have your Windows OS setup, download the latest release from this github repo, you will then have the starter Mendix App that you will be working in

[Download MPK](https://github.com/mxcrawford/Mendix-AWS-Rekognition/releases/tag/v1.0)

<a href="https://github.com/mxcrawford/Mendix-AWS-Rekognition/releases/tag/v1.0"><img src="readme-img/cf4.png"></a>

## Setup your project

1. Once you have the ```Mendix_AWS_Rekognition.mpk``` package downloaded, open Mendix Studio Pro from the Desktop.

2. Sign in with the account details you setup signing up for Mendix.

3. Import the Project package in order to create your own project

<img src="readme-img/import1.png">

4. Next, select your choice of version control and location of your project - for this exercise it is recommended to use SVN which Mendix uses in the developer portal. Select **New Mendix Teamserver**

<img src="readme-img/mx-setup-new-teamserver.png">

The project will now be created and uploaded to the repository

## The Mendix Build

In order to connect to AWS Rekognition it's important that you set up a number of constants. These constants are environment variables needed to make sure that the app can connect to the right AWS service using your AWS credentials.

### Creating your AWS Keys
To generate an AWS Access and Secret Key follow the steps below: 
1. [Create an IAM Admin](https://docs.aws.amazon.com/rekognition/latest/customlabels-dg/su-account-user.html)
2. [Create Access Keys](https://docs.aws.amazon.com/rekognition/latest/customlabels-dg/su-awscli-sdk.html)

### Security Policy for Rekognition
To keep it simple you can use stronger credentials, but in order to interact with the model ONLY, the following policy example can be used and modified:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": "rekognition:DetectCustomLabels",
            "Resource": "arn:aws:rekognition:<INSERT_REGION>:<account id>:project/<project-name>/version/*/*"
        }
    ]
}
```

### Setting your AWS Access and Secret Keys in Mendix
In order to authenticate with AWS services, it's important that requests are signed using an AWS access and secret key. In Mendix this is done using the Sig4 process. Inside this application, we have already included a module to help with this process.
1. Create access and secret key pair on AWS with access to Rekognition service.
2. Copy each of the keys.
3. With the application open inside Studio Pro expand the Marketplace modules folder item in the app explorer.
4. Then expand the AWS_Sig4 module.
5. Finally, double click on the Access Key ID and Secret Key then paste the values into each.

<img src="readme-img/app-explorer.jpg"/>

### Setting up the Rekognition constants
The Rekognition module has two constants that need to be set to ensure that the APIs can communicate with the pre-built Rekognition model. These are:
1. AWS_HostPattern - This should be the URL of the endpoint that the API is calling, this will be different depending on the AWS region used for Rekognition. The endpoint URLs can be found here: https://docs.aws.amazon.com/general/latest/gr/rekognition.html. MAKE SURE TO INCLUDE THE https://

The URL of the rekognition endpoint is usually in this format: 
```https://rekognition.{aws-region}.amazonaws.com```. But this will be slightly different if you're running from US Gov Or FIPS Cloud.

2. AWS_Region - This should be set to the region where the Rekognition AI model is deployed. The regions can be found here: https://docs.aws.amazon.com/general/latest/gr/rekognition.html

These constants can be found inside the AWS_Rekognition module in the marketplace folder. Then open up the constants folder:

<img src="readme-img/app-explorer-rekognition.jpg"/>

Once you have these constants set up you're ready to begin your build.

### Building the domain model
The first step with many Mendix projects is to start with building the data structure. Data is modeled in Mendix using domain models. Each Module in Mendix contains a domain model where you can model the entities, associations and attributes. Follow the below steps to build out the right structure. These instructions assume that you have already pre-installed Mendix Studio Pro 9.12.2+.

1. Using the App Explorer on the left-hand side open up the module **MxRekognitionDemo_Start**.
2. Double click on the **Domain model**. 
3. Drag an **Entity** from the *Toolbox* onto the *Canvas*. The *Toolbox* can be often found on the right-hand side.

<img src="readme-img/mx-build-entity.jpg"/>

4. Double click on the **Entity** to open the dialog box.
5. Change the **Name** to *Picture*

<img src="readme-img/mx-build-picture-entity.jpg"/>

6. Click the **Select** button next to **Generalization**
7. In the Search field type *Image*

<img src="readme-img/mx-build-generalization.jpg"/>

8. Double click on the **Image** entity.
9. Click the **OK** button at the bottom to close the dialog.
10. Drag another **Entity** onto the workbench.

<img src="readme-img/mx-build-label-entity.jpg"/>

11. Double click on this **Entity** and rename it to *Label*
12. Under the **Attributes** tab click the **New** button.
13. Name your first attribute **Name**.
14. Click **OK** to close the dialog.
15. Add another Attribute and call this one **Confidence**.
16. Under the Data Type Dropdown select **Decimal**.

<img src="readme-img/mx-build-label-properties.jpg"/>

17. Click **OK** and then **OK** to close both dialogs.
18. Associate the two entities by dragging the arrow **from** *Label* **to** *Picture*. This will create a relationship between these two entities.

<img src="readme-img/mx-build-label-associate.jpg"/>

### Building the User Interface
1. Open up the folder **Pages** inside the **MxRekognitionDemo_Start** module.
2. Double click on **Home_Start** to open up.
3. From the right-hand side open up the **Toolbox** and then select **Building Blocks** tab.

<img src="readme-img/mx-build-page-build.jpg"/>

4. Search and Drag the Block labeled **Label Block** to the bottom empty space.
5. Drag the other building block **Picture Block** to the space above.

<img src="readme-img/mx-build-page-build-blocks.jpg"/>

Now to give these widgets some data context, we will create a new instance of the Picture entity/object that we will use to:
1. Take a picture with and store the image in
2. Show the Label data that we receive from Rekognition that is associated with our picture (This is why we connected the Label and Picture Entity together in the Domain Model in our previous steps)

3. We need to tell the page the context object that we plan to work with (of type Picture) and for this we use a **Data View** and connect this up to our Picture object that we created in the Domain Model. Click on the **Widgets** tab on the right-hand side.
4. Drag on a **Data view** widget onto the page at the top.

<img src="readme-img/mx-build-page-dataview.jpg"/>

8. Double click on the Date View widget to open up the properties dialog.
9. Under **Data source** section, Select Type: **Nanoflow**. You can find more information on Nanoflows here: [Nanoflows](https://docs.mendix.com/refguide/nanoflows/)

Our Nanoflow will allow us to create a new Picture context object to do what we need to do, every time the page loads

10. Click the **Select** button.

<img src="readme-img/mx-build-page-datasource.jpg"/>

11. Click the **New** button at the bottom of the popup.
12. Give the Nanoflow a name like *DSO_NewPicture*. You can find more information on naming conventions here: [Best practices](https://docs.mendix.com/howto/general/dev-best-practices/#341-entity-event-microflows)

<img src="readme-img/mx-build-page-get-picture.jpg"/>

13. Click the **Show** button to open up the nanoflow and close the dialog.

<img src="readme-img/mx-build-page-nanoflow.jpg"/>

14. Using the Toolbox drag on a **Create Object** action.

<img src="readme-img/mx-build-page-create-action.jpg"/>

15. Double click on the action and set the entity type to our new **Picture** entity.

<img src="readme-img/mx-build-page-select-entity.jpg"/>

16. Double click on the **Endpoint** represented by a red dot. 
17. Configure it to return an **Object** 

<img src="readme-img/mx-build-page-nanoflow-return.jpg"/>

18. Set the value to the newly created Object $NewPicture. You can click Ctrl + Space and select it from dropdown. 

<img src="readme-img/mx-build-page-nanoflow-set-return.jpg"/>

19. Click File -> Save All.

This Nanoflow is now complete, and we can move onto our next step

20.  Open up the **Home_Start** page again.
21.  Drag the entiere existing layout into the Dataview.

<img src="readme-img/mx-build-layout-drag.gif"/>

22. Double click on the Picture *control* and connect it to the Picture *entity*.

<img src="readme-img/mx-pic-cntrlp-toentity.png"/>

23. Double click on the **List view**. Select **Data source** tab. Next to **Entity (path)** click **Select** and pick entity *Label*

<img src="readme-img/mx-build-page-listview-association.jpg"/>

**Select NO when asked to automatically fill the contents**

<img src="readme-img/mx-build-page-listview-autogenerate.jpg"/>

24. Configure the left parameter in the ListView by double-clicking on the **Text item**, then use then connect Parameter {1} up to **Name**.
Caption: {1}
Parameter type: Attribute path
Attribute path: MxRekogniitonDemo_Start.Label.Name
mx-build-page-listview-autogenerate.jpg
<img src="readme-img/mx-build-page-setting-labels.jpg"/>

25. Configure the right parameter in the ListView by double-clicking on the **Text item**, then use then connect Parameter {1} up to **Confidence**.

<img src="readme-img/mx-build-page-setting-labels-confidence.jpg"/>

### Building the logic
Logic in Mendix is defined using Microflows for server-side logic and Nanoflows for client-side. Both of these concepts use the same modeling paradigm. Allowing you to define logic using actions, decisions and loops.

To perform the logic needed we'll create a Nanoflow which will open up the camera, save the picture, process it by Rekognition, and save the results. Here are the steps:

1. Right-click on the **Take a picture** button and click **Edit on click action**.
2. Select from the dropdown **Call a Nanoflow**.
3. Click the **New** button.
4. Enter the name *ACT_TakePicture* and click **OK**.

<img src="readme-img/mx-build-page-edit-action.jpg"/>

5. Open up the newly built Nanoflow.
6. Drag and Drop from the Toolbox a **Take Picture** action. The box has to be in the middle of an arrow as on the screenshot.

<img src="readme-img/mx-build-logic-take-picture.jpg"/>

7. Double-Click on the **Detect Custom Labels** Activity box to Configure the parameters as follows:
    - Picture = $Picture
    - Show Confirmation Screen = false
    - Picture Quality = low
    - Maximum width = empty
    - Maximum height = empty

<img src="readme-img/mx-build-logic-take-picture-options.jpg"/>

8. From the Toolbox drag the detect custom labels action and configure as follows:
    - ProjectARN = Your Rekognition ARN. 
    Copy paste your Rekognition model.
    
    **Make sure to include single quotes when inserting a text/string value**

Example:

    ```
    'arn:aws:rekognition:us-west-2:123456:project/awsworkshop-mendix-aicv/version/awsworkshop-mendix-aicv.2022-07-19T11.24.01/1658244241480'
    ```

    - Image = $Picture
    - MaxResults = 10
    - MinConfidence = 0
    - AWS_Region = your region.
    Press *Control + Space* and type *AWS_Rekognition.AWS_Region.* and select your region.

    Lastly, the Detect Custom Labels action returns a list of labels, make sure to give it a clear name

<img src="readme-img/mx-build-logic-Rekognition.jpg"/>

9. Drag a **Loop** activity from the **Toolbox** to the microflow and connect it to the CustomLabel List.

<img src="readme-img/mx-logic-loop-1.gif"/>

10. Inside the loop drag a **Retrieve** action from the **Toolbox** to retrieve the **BoundingBox**.
11. Connect up the retrieve action by double-clicking on the action, selecting **By association**, clicking **Select**, and selecting the **Bounding Box** association.


<img src="readme-img/mx-logic-boundingbox-1.gif"/>
<img src="readme-img/mx-build-logic-retrieve-action.jpg"/>



12. Drag on a **Create Object** action from the **Toolbox** into the loop and draw a line from the bounding box to the "Create" action.

<img src="readme-img/mx-build-logic-create-action.jpg"/>

13. Configure the activity by selecting the Entity "Label".
14. Set 3 Members to the following:
    - Label_Picture = $Picture
    - Confidence = $IteratorCustomLabel/Confidence
    - Name = $IteratorCustomLabel/Name
  
15. Make sure to Commit the Label Object by checking the "Commit" Checkbox


<img src="readme-img/mx-build-logic-create-configure.jpg"/>

15. Finally add a bounding box activity and configure as follows:
    - Class name = 'img-container'
    - Bounding box = $BoundingBox
    - Custom label = $IteratorCustomLabel
    - High Confidence Threshold = 80
    - Medium Confidence Threshold = 50

<img src="readme-img/mx-build-logic-canva.png"/>
<img src="readme-img/mx-build-logic-bounding-box.jpg"/>

16. We are now complete so we need to run the project in the cloud. Click the **Publish** button on the top right.

<img src="readme-img/mx-build-publish-app.jpg"/>

17. You will then see a message informing you that the deployment is in process.

<img src="readme-img/mx-build-publish-app-inprogress.jpg"/>

18. Click the dropdown near the view app and select view on device.

<img src="readme-img/mx-build-run-view-on-device.jpg"/>

19. A QR code will be generated and displayed. Scan this with your mobile phone.

<img src="readme-img/mx-build-run-qrcode.jpg"/>

20. Congratulations you can now use your newly built app!

<img src="readme-img/end-result.jpg"/>

# Bonus

If you're streaking ahead, you can always try out the bonus activities

[Click here for the Bonus activity](https://github.com/mxcrawford/Mendix-AWS-IoT/blob/main/README.md#mendix-aws-iot-bonus)



# Clean up

## Amazon Rekogniton Clean up


1. Open Amazon Rekognition Console.  Select a region where you built a project on the top.
2.  Select Custom Labels from the menu on the left side and choose Project.


<img src="readme-img/cleanup-rek1.png"/>

3. Under models, click on the  hyperlink of the model name.

<img src="readme-img/cleanup-rek2.png"/>

4. Click on the Use model tab. Click **Stop**.

<img src="readme-img/cleanup-rek3.png"/>

5. In the popup window, type *stop*. Wait for a model to be  stopped. 

6. Once the model is stopped, go back to the project. Select a model with a checkbox and click **Delete model** button.

<img src="readme-img/cleanup-rek4.png"/>

7. In the popup window, type *delete*. Wait for a model to be  stopped. 

## Amazon S3 Clean up

1. Open AWS Console for Amazon S3. Select a bucket that contains an image dataset. Click on **Empty** button at the top.

<img src="readme-img/cleanup-s3-1.png"/>

2. In the new window type *permanently delete* to delete objects in the bucket.

3. Once bucket will be emptied, go back to Amazon S3 bucket list and select your bucket. Click on **Delete** button at the top.

<img src="readme-img/cleanup-s3-2.png"/>

## Amazon EC2 Clean up

1. Open EC2 Console. Select *Frankfurt* region at the top. Click on *Instances (running)*

<img src="readme-img/cleanup-ec2-1.png"/>

2. Select your instance from the list. Expand *Instance state* dropdown at the top and click **Terminate instance**

<img src="readme-img/cleanup-ec2-2.png"/>

3. In the popup window, confirm **Terminate**

# Frequently Asked Questions (FAQ)

See our [FAQ](FAQ.md) section

# Troubleshooting

See our [Troubleshooting secion in the FAQ page](FAQ.md#feedback) section

# Your feedback

If you have a question that is NOT answered below, or if you have other feedback regarding the workshop, please feel free to get in touch with us [here](https://mendix.com)
