#!/bin/bash
read -p "Enter your IAMStackName: " sname
IAMStackName="$sname"-csye6225-iam

read -p "Enter your Bucket_Name: " bname
BucketName="$bname"

printf "\n\n"
AWSAccountID=`aws sts get-caller-identity --output text --query Account`
printf "Using AWSAccountID from AWS-CLI:  $AWSAccountID "

printf "\n\n"
AWSRegion=`aws configure get region`
printf "Using AWSRegion from AWS-CLI:  $AWSRegion "

printf "\n\n"
printf "......................  Please wait  ...................... \n"
    aws cloudformation create-stack --stack-name $IAMStackName --template-body file://csye6225-cf-iam.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=BucketName,ParameterValue=$BucketName ParameterKey=AWSAccountID,ParameterValue=$AWSAccountID ParameterKey=AWSRegion,ParameterValue=$AWSRegion
    aws cloudformation wait stack-create-complete --stack-name $IAMStackName
    if [ $? -eq 0 ]; then
      printf "\n\n"
      printf "<<<<<<<<<<<<<<<<<<<<<<  Stack completed successfully  >>>>>>>>>>>>>>>>>>>>>> \n\n"
      printf "\n\n"
      exit
    else
      printf "\n\n"
      printf "!!!!!!!!!!!!!!!!!!!!!!  Stack creation failed  !!!!!!!!!!!!!!!!!!!!!! \n"
      printf "\n\n"
      exit
    fi
printf "\n" 
